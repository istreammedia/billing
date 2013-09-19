package org.mifosplatform.billing.message.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BillingMessageData {
	
	private String templateDescription;
	private String subject;
	private String header;
	private String body;
	private String footer;
	private String parameter;
	private ArrayList<String> arraydata;
	private Long clientId;
	private Long id;
	private String messageParameters;
	private List<BillingMessageData> messageParams;
	private long deleteButtonId;
	

	
	public BillingMessageData(Long id, String templateDescription, String subject,
			String header, String body, String footer, String messageParameters) {
		
		// TODO Auto-generated constructor stub
		this.id=id;
		this.templateDescription=templateDescription;
		this.subject=subject;
		this.header=header;
		this.body=body;
		this.footer=footer;
		this.messageParameters=messageParameters;
		
	}
	
	public String getMessageParameters(){
		return this.messageParameters;
	}
	
	public Long getId(){
		return id;
	}

	public ArrayList<String> getArraydata() {
		return arraydata;
	}

	public BillingMessageData(Long messageTemplateId,String parameterName) {
		this.deleteButtonId=messageTemplateId;
		// TODO Auto-generated constructor stub
		this.parameter=parameterName;
	}

	public BillingMessageData(ArrayList<String> rowdata) {
		// TODO Auto-generated constructor stub
		this.arraydata=rowdata;
	}

	public BillingMessageData(Long commandId) {
		// TODO Auto-generated constructor stub
		this.clientId=commandId;
	}
	public Long getClientId(){
		return clientId;
	}

	public String getParameter() {
		return parameter;
	}

	public String getTemplateDescription() {
		return templateDescription;
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

	public void setMessageParams(List<BillingMessageData> messageParams) {
	this.messageParams=messageParams;
		
	}
	
}
