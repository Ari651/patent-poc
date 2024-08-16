databaseChangeLog = {

    changeSet(author: "aribu (generated)", id: "1723066262028-1") {
        createTable(tableName: "PATENT") {
            column(autoIncrement: "true", name: "ID", type: "INT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "CONSTRAINT_8")
            }

            column(name: "CREATED_BY", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "DATE_CREATED", type: "TIMESTAMP") {
                constraints(nullable: "false")
            }

            column(name: "DATE_OF_PUBLICATION", type: "TIMESTAMP")

            column(name: "DATE_PATENT_FILED", type: "TIMESTAMP")

            column(name: "DESCRIPTION", type: "VARCHAR(1000000000)") {
                constraints(nullable: "false")
            }

            column(name: "INVENTORS", type: "VARCHAR(1425)")

            column(name: "LAST_UPDATED", type: "TIMESTAMP")

            column(name: "LAST_UPDATED_BY", type: "INT")

            column(name: "PATENT_NUMBER", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "PATENT_STATUS_ID", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "PATENT_TITLE", type: "VARCHAR(500)") {
                constraints(nullable: "false")
            }

            column(name: "PRIOR_ENTRY", type: "BOOLEAN")

            column(name: "PROGRESS_REPORT_ID", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "BASELINE_DATE", type: "date")
        }
    }
}
