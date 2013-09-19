package org.mifosplatform.billing.billingmaster.command;

import org.joda.time.LocalDate;

public class BillMasterCommand {

	private LocalDate dueDate;
	private String message;

	public BillMasterCommand(LocalDate dueDate, String message) {
	this.dueDate=dueDate;
	this.message=message;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public String getMessage() {
		return message;
	}



}
