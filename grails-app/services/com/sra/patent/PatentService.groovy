package com.sra.patent

import groovy.json.JsonBuilder
import groovy.json.JsonOutput

class PatentService {

    /**
     * Searches for a granted patent by its Patent ID, Application ID (etc?)
     * @param id A unique character sequence that identifies the patent
     * @return the published data regarding the patent (or null)
     */
    def fetchPatentInfo(String id, String title) {
        JsonBuilder builder = new JsonBuilder()
        builder {
            f 'patent_id', 'patent_title', 'patent_date', 'patent_abstract', 'patent_detail_desc_length',
                    'patent_earliest_application_date', 'patent_term_extension', 'patent_type', 'patent_year',
                    'inventors.inventor_name_first', 'inventors.inventor_name_last', 'pct_data', 'application'
            o { size 100 }
            q {
                _and List.of(
                        {'inventors.inventor_name_last' 'Dodge'},
                        { _text_all { patent_title title } },
                        {_or List.of(
                                { patent_id id },
                                { patent_id id },
                                { 'application.application_id' id },
                                { 'pct_data.pct_doc_number' id }
                            )
                        }
                )
            }
            s List.of(patent_id:'asc')
        }

        log.info JsonOutput.prettyPrint(builder.toString())

        //TODO API Call and Output
    }
}