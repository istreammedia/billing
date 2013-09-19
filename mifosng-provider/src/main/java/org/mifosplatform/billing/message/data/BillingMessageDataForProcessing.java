package org.mifosplatform.billing.message.data;

import java.util.ArrayList;
import java.util.List;

public class BillingMessageDataForProcessing {

	
	private final String messageTo;
	private final String messageFrom;
	private final String subject;
	private final String header;
	private final String body;
	private final String footer;
	private final Long id;
	
	
	public BillingMessageDataForProcessing(Long id, String messageto,
			String messagefrom, String subject, String header, String body,
			String footer) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.messageFrom=messagefrom;
		this.messageTo=messageto;
		this.body=body;
		this.footer=footer;
		this.header=header;
		this.subject=subject;
		
	}
	
	public String getMessageTo() {
		return messageTo;
	}

	public String getMessageFrom() {
		return messageFrom;
	}

	public String getSubject() {
		return subject;
	}

	public String getHeader() {
		return header;
	}

	public String getBody() {
		return body;
	}

	public String getFooter() {
		return footer;
	}

	public Long getId() {
		return id;
	}

	
	
}
