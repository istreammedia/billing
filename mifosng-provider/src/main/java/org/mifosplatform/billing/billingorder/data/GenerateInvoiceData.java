package org.mifosplatform.billing.billingorder.data;

import org.joda.time.LocalDate;

public class GenerateInvoiceData {
	
	private final Long clientId;
	private final LocalDate nextBillableDay;
	
	public GenerateInvoiceData( final Long clientId, final LocalDate nextBillableDay ) {
		this.clientId = clientId;
		this.nextBillableDay = nextBillableDay;
	}

	public Long getClientId() {
		return clientId;
	}

	public LocalDate getNextBillableDay() {
		return nextBillableDay;
	}

}
