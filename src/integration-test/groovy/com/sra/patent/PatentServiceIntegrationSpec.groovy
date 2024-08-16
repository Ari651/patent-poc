package com.sra.patent

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class PatentServiceIntegrationSpec extends Specification {
    PatentService patentService
    PatentViewClient patentViewClient


    void "try a real call"() {
        expect:
        patentService.fetchPatentInfo('12/994,661', 'Wnt protein signalling inhibitors') != null
    }

    void "try looping through the patents"() {
        when:
        patentService.processExistingPatentData()
        then:
        noExceptionThrown()
    }
}
