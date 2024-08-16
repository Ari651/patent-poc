package com.sra.patent

import grails.gorm.services.Service

@Service(Patent)
interface PatentDataService {
    List<Patent> getAll()
}