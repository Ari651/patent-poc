package com.sra.patent

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

/*
This is just a way of outputting the JSON to verify the format for the PatentsView API
 */
class PatentServiceSpec extends Specification implements ServiceUnitTest<PatentService> {

    void setup() {
        service.patentViewClient = Mock(PatentViewClient)
    }

    void "just testing builder output"() {
        expect:
//        service.fetchPatentInfo('2726987', 'Wnt protein signalling inhibitors') == null
        service.fetchPatentInfo('1,2/99/4,6,61', 'Wnt protein signalling inhibitors') == null
    }
}