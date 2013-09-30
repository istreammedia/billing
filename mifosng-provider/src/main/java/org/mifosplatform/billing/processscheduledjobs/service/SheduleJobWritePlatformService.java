package org.mifosplatform.billing.processscheduledjobs.service;

public interface SheduleJobWritePlatformService {

	//void runSheduledJobs();

	void processInvoice();

	void processRequest();

	void processResponse();

	void processSimulator();

	void generateStatment();

	void processingMessages();

	void processingAutoExipryOrders();

	void newJob();

	
}
