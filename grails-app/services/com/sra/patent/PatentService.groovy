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
        id = id.trim()
        String noComma = id.replaceAll(/,/, "")
        String noSlash = id.replaceAll(/\//, "")
        String none = id.replaceAll(/[,\/]/, "")
        JsonBuilder builder = new JsonBuilder()
        builder {
            f 'patent_id', 'patent_title', 'patent_date', 'patent_abstract', 'patent_detail_desc_length',
                    'patent_earliest_application_date', 'patent_term_extension', 'patent_type', 'patent_year',
                    'inventors.inventor_name_first', 'inventors.inventor_name_last', 'pct_data', 'application'
            o { size 100 }
            q {
                _and List.of(
//                        {'inventors.inventor_name_last' 'Dodge'},
                        { _text_all { patent_title title } },
                        {_or List.of(
                                { patent_id id },
                                { patent_id noComma },
                                { patent_id noSlash },
                                { patent_id none },
                                { 'application.application_id' id },
                                { 'application.application_id' noComma },
                                { 'application.application_id' noSlash },
                                { 'application.application_id' none },
                                { 'pct_data.pct_doc_number' id },
                                { 'pct_data.pct_doc_number' noComma },
                                { 'pct_data.pct_doc_number' noSlash },
                                { 'pct_data.pct_doc_number' none }
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