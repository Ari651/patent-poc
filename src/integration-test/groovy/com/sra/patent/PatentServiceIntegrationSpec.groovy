package com.sra.patent

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class PatentServiceIntegrationSpec extends Specification {
    PatentService patentService
    PatentViewClient patentViewClient


    void "try a real call"() {
        expect:
//        patentService.fetchPatentInfo('12/994,661', 'Wnt protein signalling inhibitors') != null
        patentService.fetchPatentInfo('9045416', 'Wnt protein signalling inhibitors') != null
//        patentService.fetchPatentInfo('US 2013 016 5570 A1', 'System, Method and Apparatus for Tracking Targets During Treatment Using a Radar Motion Sensor') != null
    }

    void "try looping through the patents"() {
        when:
        patentService.processExistingPatentData()
        then:
        noExceptionThrown()
    }
}
