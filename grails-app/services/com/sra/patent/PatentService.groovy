package com.sra.patent

import grails.gorm.transactions.Transactional

//Grails @Transactional so the service bean can call its own methods

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.springframework.beans.factory.annotation.Autowired

import static io.micronaut.http.HttpHeaders.RETRY_AFTER
import static io.micronaut.http.HttpStatus.TOO_MANY_REQUESTS

@Transactional
class PatentService {

    //@Autowired needed for Micronaut declarative client
    @Autowired
    PatentViewClient patentViewClient

    PatentDataService patentDataService

    /**
     * Searches for a granted patent by its Patent ID, Application ID (etc?)
     * @param id A unique character sequence that identifies the patent
     * @param title The title of the patent/application
     * @return the published data regarding the patent (or null)
     */
    def fetchPatentInfo(String id, String title) {
        log.info "As entered: $id"
        String lookupId = id.trim() - 'WO' - 'A1' - 'A2' - 'B1' - 'B2'
        //remove 'US' from the front only (case insensitive)
        lookupId = lookupId.trim().replaceAll(' ', '').replaceAll(/^([Uu][Ss])/, '')
        log.info "Using: $lookupId"
        String noComma = lookupId.replaceAll(/,/, "")
        String noSlash = lookupId.replaceAll(/\//, "")
        String none = lookupId.replaceAll(/[,\/]/, "")
        JsonBuilder builder = new JsonBuilder()
        builder {
            f 'patent_id', 'patent_title', 'patent_date', 'patent_abstract', 'patent_detail_desc_length',
                    'patent_earliest_application_date', 'patent_term_extension', 'patent_type', 'patent_year',
                    'inventors.inventor_name_first', 'inventors.inventor_name_last', 'pct_data', 'application'
            o { size 100 }
            q {
                _or List.of(
                    { patent_id lookupId },
                    { patent_id noComma },
                    { patent_id noSlash },
                    { patent_id none },
                    { 'application.application_id' lookupId },
                    { 'application.application_id' noComma },
                    { 'application.application_id' noSlash },
                    { 'application.application_id' none },
                    { 'pct_data.pct_doc_number' lookupId },
                    { 'pct_data.pct_doc_number' noComma },
                    { 'pct_data.pct_doc_number' noSlash },
                    { 'pct_data.pct_doc_number' none }
                )
            }
            s List.of(patent_id:'asc')
        }

        String query = builder.toString()
        log.debug JsonOutput.prettyPrint(query)

        try {
            doCall(query)
        } catch(HttpClientResponseException hcre) {
            log.error "${hcre.status?.code}: ${hcre.status?.reason}"
            if(hcre.status == TOO_MANY_REQUESTS) {
                //Read Header for seconds to wait
                int timeToWait = (hcre.response?.header(RETRY_AFTER) as int) ?: 0
                //add one second.
                timeToWait++
                //QUICK & DIRTY
                log.info("Wait for $timeToWait seconds and retry")
                sleep(1000 * timeToWait)
                //Retry once
                try {
                    log.info "Trying again..."
                    doCall(query)
                } catch (HttpClientResponseException ignore) {
                    //give up.
                    log.error "Retry failed for ID $lookupId"
                }
            }
        }
    }

    private def doCall(String query) throws HttpClientResponseException {
        def response = patentViewClient.search(query)
        log.debug(response as String)

        response
    }

    @SuppressWarnings('GroovyAssignabilityCheck')
    def processExistingPatentData() {
        List<Map<String, String>> found = []
        def ambiguous = []
        List<Map<String, String>> unmatched = []

        //QUICK & DIRTY rate limit
        int count = 0
        log.debug("We're logging at the DEBUG level")
        log.info("${Patent.count} Patents")
        patentDataService.all.each {
            def info = fetchPatentInfo(it.patentNumber, it.patentTitle)
            if(info && !info.error){
                String applications
                String pctDocs
                if(info.count == 1) {
                    //Found our patent
                    def patent = info.patents[0]
                    applications = patent?.application?.collect { app -> app.application_id }?.join(', ')
                    pctDocs = patent?.pct_data?.collect { pct -> pct.pct_doc_number }?.join(', ')

                    Map foundItem = [patentId: it.id, found: it.patentNumber, patentNumber: patent.patent_id, applications: applications, pct_docs: pctDocs, title: patent.patent_title]
                    log.info("FOUND: ${foundItem as String}")
                    found << foundItem

                } else if(info.count > 1 || info.total_hits > 1){
                    //Record the ambiguities
                    List<Map<String,String>> hits = []
                    info.patents?.each { patent ->
                        applications = patent?.application?.collect {app -> app.application_id }?.join(', ')
                        pctDocs = patent?.pct_data?.collect { pct -> pct.pct_doc_number }?.join(', ')
                        def hit = [patentId: it.id, patentNumber: patent.patent_number, applications: applications, pct_docs: pctDocs, title: patent.patent_title]
                        log.info(hit as String)
                        hits << hit
                    }
                    ambiguous << [(it.patentNumber): hits]

                } else {
                    //Record the exceptions
                    Map miss = [patentId: it.id, patentNumber: it.patentNumber, title: it.patentTitle]
                    log.info(miss as String)
                    unmatched << miss
                }
            } else {
                log.error("Error reponse for patent $it.patentNumber")
            }
            if(count == 45) {
                sleep(15000)
                count = 0
            }
        }

        log.info("${found.size()} matched:")
        found.each {
            log.info(it as String)
        }
        3.times {
            log.info('---')
        }
        log.info("${ambiguous.size()} unclear:")
        ambiguous.each {
            log.info(it as String)
        }
        3.times {
            log.info('---')
        }
        log.info("${unmatched.size()} unmatched:")
        unmatched.each {
            log.info(it as String)
        }

    }

       /* [
                error: false,
                count: 1,
                total_hits: 1,
                patents: [
                        [
                                patent_id: '8445491',
                                patent_title: 'Wnt protein signalling inhibitors',
                                patent_type: 'utility',
                                patent_date: '2013-05-21',
                                patent_year: 2013,
                                patent_abstract: 'The present invention generally relates to protein signalling. In particular, compounds that inhibit the Wnt protein signalling pathway are disclosed. Such compounds may be used in the treatment of Wnt protein signalling-related diseases and conditions such as cancer, degenerative diseases, type II diabetes and osteopetrosis.',
                                patent_detail_desc_length: 109437,
                                patent_earliest_application_date: '2009-05-27',
                                patent_term_extension: 164,
                                application: [
                                        [
                                                application_id: '12/994661',
                                                application_type: '12',
                                                filing_date: '2009-05-27',
                                                series_code: '12',
                                                rule_47_flag: false,
                                                filing_type: '12'
                                        ]
                                ],
                                inventors: [
                                        [inventor_name_first: 'Chuo', inventor_name_last: 'CHEN'],
                                        [inventor_name_first: 'Lawrence G.', inventor_name_last: 'Lum'],
                                        [inventor_name_first: 'Baozhi', inventor_name_last: 'Chen'],
                                        [inventor_name_first: 'Wei', inventor_name_last: 'Tang'],
                                        [inventor_name_first: 'Michael G.', inventor_name_last: 'Roth'],
                                        [inventor_name_first: 'Michael E.', inventor_name_last: 'Dodge']
                                ],
                                pct_data: [
                                        [
                                                published_filed_date: '2009-05-27',
                                                pct_102_date: null,
                                                pct_371_date: '2011-02-21',
                                                application_kind: '00',
                                                pct_doc_number: 'PCT/US2009/045340',
                                                pct_doc_type: 'pct_application'
                                        ],
                                        [
                                                published_filed_date: '2009-12-23',
                                                pct_102_date: null,
                                                pct_371_date: null,
                                                application_kind: 'A',
                                                pct_doc_number: 'WO2009/155001',
                                                pct_doc_type: 'wo_grant'
                                        ]
                                ]
                        ]
                ]
        ]
    }*/

}