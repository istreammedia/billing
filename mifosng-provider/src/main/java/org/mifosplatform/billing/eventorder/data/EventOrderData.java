package org.mifosplatform.billing.eventorder.data;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

public class EventOrderData {
	
	private final Long id;
	private final String eventName;
	private final LocalDate bookedDate;
	private final BigDecimal eventPrice;
	private final String chargeCode;
	private final String status;

	public EventOrderData(Long orderid, LocalDate bookedDate, String eventName,
			BigDecimal bookedPrice, String chargeCode, String status) {
		
		this.id=orderid;
		this.eventName=eventName;
		this.bookedDate=bookedDate;
		this.eventPrice=bookedPrice;
		this.chargeCode=chargeCode;
		this.status=status;
		
	}

	public Long getId() {
		return id;
	}

	public String getEventName() {
		return eventName;
	}

	public LocalDate getBookedDate() {
		return bookedDate;
	}

	public BigDecimal getEventPrice() {
		return eventPrice;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public String getStatus() {
		return status;
	}

}
