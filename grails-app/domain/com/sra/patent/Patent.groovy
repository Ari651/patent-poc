package com.sra.patent

class Patent {
    static mapping = {
        table name: 'patent', schema: 'cgms'
        version false
        id generator: 'identity', column: 'id', sqlType: 'int'
    }

    String patentNumber
    String patentTitle

    String inventors
    String description

    Date dateOfPublication
    Date datePatentFiled

    Integer patentStatusId
    Boolean priorEntry
    Integer progressReportId
    Date baselineDate

    Integer createdBy
    Date dateCreated
    Date lastUpdated
    Integer lastUpdatedBy

    static constraints = {
        inventors nullable: true
        dateOfPublication nullable: true
        datePatentFiled nullable: true
        priorEntry nullable: true
        lastUpdated nullable: true
        lastUpdatedBy nullable: true
    }




}