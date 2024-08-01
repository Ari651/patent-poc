package com.sra.patent

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification
import spock.lang.Subject

/*
This is just a way of outputting the JSON to verify the format for the PatentsView API
 */
class PatentServiceSpec extends Specification implements ServiceUnitTest<PatentService> {
    @Subject
    PatentService service

    void setup() {
        service = new PatentService()
    }

    void "just testing output"() {
        expect:
        service.fetchPatentInfo('11751759') == null
    }
}