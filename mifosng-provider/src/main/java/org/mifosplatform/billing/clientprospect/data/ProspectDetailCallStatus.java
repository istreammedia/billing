package org.mifosplatform.billing.clientprospect.data;

public class ProspectDetailCallStatus {
	
	private Long statusId;
	private String callStatus;
	
	public ProspectDetailCallStatus() {
		// TODO Auto-generated constructor stub
	}
	
	public ProspectDetailCallStatus(Long statusId,String callStatus){
		this.statusId = statusId;
		this.callStatus = callStatus;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}
}
