package org.mifosplatform.billing.message.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;


@Entity
@Table(name = "b_message_data")
public class BillingMessage extends AbstractAuditableCustom<AppUser, Long> {

	
	@Column(name = "message_to")
	private String messageTo;
	
	@Column(name = "message_from")
	private String messageFrom;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "header")
	private String header;

	@Column(name = "body")
	private String body;
	
	@Column(name = "footer")
	private String footer;
	
	@Column(name = "subject")
	private String subject;
	
	@ManyToOne
    @JoinColumn(name="msgtemplate_id")
    private BillingMessageTemplate billingMessageTemplate;
	
	public BillingMessage(){
		//default-constructor
	}

	public BillingMessage(String header,String body,String footer,
			String messageFrom,String messageTo,String subject,String status, BillingMessageTemplate billingMessageTemplate) {
        
		this.header=header;
		this.body=body;
		this.footer=footer;
		this.messageFrom=messageFrom;
		this.messageTo=messageTo;
		this.subject=subject;
		this.status=status;
		this.billingMessageTemplate=billingMessageTemplate;
	}

	public String getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}

	public String getMessageFrom() {
		return messageFrom;
	}

	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public BillingMessageTemplate getBillingMessageTemplate() {
		return billingMessageTemplate;
	}

	public void setupdate(BillingMessageTemplate billingMessageTemplate) {
		this.billingMessageTemplate = billingMessageTemplate;
	}

	public void updateStatus() {
		// TODO Auto-generated method stub
		this.status="Y";
		
	}
	
	
	
}
