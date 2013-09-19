package org.mifosplatform.billing.clientprospect.data;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ProspectDetailData {
	
	
	private Long id;
	private Long prospectId;
	private String callStatus;
	private Date nextTime;
	private String notes;
	private String assignedTo;
	
	
	private Collection<ProspectDetailAssignedToData> assignedToData;
	private Collection<ProspectDetailCallStatus> callStatusData;
	private Collection<ProspectDetailData> prospectDetailData;
	
	
	public ProspectDetailData() {
	
	}
	
	public ProspectDetailData(Long id, Long prospectId, String callStatus, Date nextTime, String notes, String assignedTo){
		this.id = id;
		this.prospectId = prospectId;
		this.callStatus = callStatus;
		this.nextTime = nextTime;
		this.notes = notes;
		this.assignedTo = assignedTo;
	}
	
	public ProspectDetailData(Long id, Long prospectId, String callStatus, Date nextTime, String notes, String assignedTo, Collection<ProspectDetailAssignedToData> assignedToData, Collection<ProspectDetailCallStatus> callStatusData){
		this.id = id;
		this.prospectId = prospectId;
		this.callStatus = callStatus;
		this.nextTime = nextTime;
		this.notes = notes;
		this.assignedTo = assignedTo;
		this.assignedToData = assignedToData;
		this.callStatusData = callStatusData;
	}
	
	
	
	public ProspectDetailData(List<ProspectDetailData> prospectDetailData2) {
		this.prospectDetailData = prospectDetailData2;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProspectId() {
		return prospectId;
	}
	public void setProspectId(Long prospectId) {
		this.prospectId = prospectId;
	}
	public String getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}
	public Date getNextTime() {
		return nextTime;
	}
	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Collection<ProspectDetailAssignedToData> getAssignedToData() {
		return assignedToData;
	}

	public void setAssignedToData(Collection<ProspectDetailAssignedToData> assignedToData) {
		this.assignedToData = assignedToData;
	}

	public Collection<ProspectDetailCallStatus> getCallStatusData() {
		return callStatusData;
	}

	public void setCallStatusData(Collection<ProspectDetailCallStatus> callStatusData) {
		this.callStatusData = callStatusData;
	}

	public Collection<ProspectDetailData> getProspectDetailData() {
		return prospectDetailData;
	}

	public void setProspectDetailData(
			Collection<ProspectDetailData> prospectDetailData) {
		this.prospectDetailData = prospectDetailData;
	}
	
	
	
	

}
