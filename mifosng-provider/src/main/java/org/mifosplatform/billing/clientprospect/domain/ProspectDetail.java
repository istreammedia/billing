package org.mifosplatform.billing.clientprospect.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name="b_prospect_detail")
public class ProspectDetail extends AbstractAuditableCustom<AppUser, Long>{
	
	
	@Column(name="prospect_id")
	private Long prospectId;
	
	@Column(name="call_status")
	private String callStatus;
	
	@Column(name="next_time")
	private Date nextTime;
	
	@Column(name="notes")
	private String notes;
	
	@Column(name="assigned_to")
	private String assignedTo;
	
	
	ProspectDetail(){}
	
	ProspectDetail(Long prospectId, String callStatus, Date nextTime, String notes, String assignedTo){
		this.prospectId = prospectId;
		this.callStatus = callStatus;
		this.nextTime = nextTime;
		this.notes = notes;
		this.assignedTo = assignedTo;
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
	
	public static ProspectDetail fromJson(JsonCommand command, Long prospectId) throws ParseException{
		
		String callStatus = command.stringValueOfParameterNamed("callStatus");
		String startDateString = command.stringValueOfParameterNamed("preferredCallingTime");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nextTime = df.parse(startDateString);
		String notes = command.stringValueOfParameterNamed("notes");
		String assignedTo = command.stringValueOfParameterNamed("assignedTo");
		
		return new ProspectDetail(prospectId, callStatus, nextTime, notes, assignedTo);
		
	}
	
	
	public Map<String, Object> update(JsonCommand command){
		return null;
	}
	

}
