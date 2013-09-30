package org.mifosplatform.infrastructure.jobs.service;

public enum JobName {

    INVOICE("Invoice"),REQUESTOR("Requestor"),RESPONSOR("Responser"),SIMULATOR("Simulator"),
    GENERATE_STATMENT("Generate Statment"),MESSANGER("Messanger"),AUTO_EXIPIRY("Auto Exipiry"),ALL("All");

    private final String name;

    JobName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
