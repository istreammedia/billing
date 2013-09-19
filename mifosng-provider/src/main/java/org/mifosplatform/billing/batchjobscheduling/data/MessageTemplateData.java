package org.mifosplatform.billing.batchjobscheduling.data;

public class MessageTemplateData {

	
	
	private Long id;
	private String msgTemplateDescription;
	
	
	public MessageTemplateData() {
		// TODO Auto-generated constructor stub
	}
	
	
	public MessageTemplateData(final Long id, final String msgTemplateDescription){
		this.id = id;
		this.msgTemplateDescription = msgTemplateDescription;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMsgTemplateDescription() {
		return msgTemplateDescription;
	}
	public void setMsgTemplateDescription(String msgTemplateDescription) {
		this.msgTemplateDescription = msgTemplateDescription;
	}
	
	
	
}
