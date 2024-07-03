package com.sra.patent

import groovy.json.JsonBuilder
import groovy.json.JsonOutput

class PatentService {

    /**
     * Searches for a granted patent by its Patent ID, Application ID (etc?)
     * @param id A unique character sequence that identifies the patent
     * @return the published data regarding the patent (or null)
     */
    def fetchPatentInfo(String id) {
        JsonBuilder builder = new JsonBuilder()
        builder {
            f 'patent_id', 'patent_title', 'patent_date'
            o { size 100 }
            q {

            }
            s List.of(patent_id:'asc')
        }

        String body = JsonOutput.prettyPrint(builder.toString())
    }
}