package org.mifosplatform.billing.clientprospect.data;

public class ProspectDetailAssignedToData {

	private Long assignId;
	private String assignedTo;
	
	
	public ProspectDetailAssignedToData() {
		// TODO Auto-generated constructor stub
	}
	
	public ProspectDetailAssignedToData(Long assignedId,String assignedTo){
		
		this.assignId = assignedId;
		this.assignedTo = assignedTo;
	}

	public Long getAssignId() {
		return assignId;
	}

	public void setAssignId(Long assignId) {
		this.assignId = assignId;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
}
