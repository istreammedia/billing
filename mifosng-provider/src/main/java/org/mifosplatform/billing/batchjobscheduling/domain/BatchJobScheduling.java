package org.mifosplatform.billing.batchjobscheduling.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.dao.DataIntegrityViolationException;


@Entity
@Table(name="b_schedule_jobs")
public class BatchJobScheduling extends AbstractAuditableCustom<AppUser, Long>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="batch_name", nullable=false, length=20)
	private String batchName;
	
	@Column(name="process", nullable=false, length=20)
	private String process;
	
	@Column(name="schedule_type", nullable=false, length=1)
	private String scheduleType;
	
	@Column(name="status", nullable=false, length=1)
	private String status;
	
	@Column(name="schedule_time")
	private Date scheduleTime;
	
	@Column(name="process_params",length=255)
	private String msgTemplateDescription;
	
	public BatchJobScheduling() {
	
	}
	
	public BatchJobScheduling(final String batchName, final String process, String scheduleType, final String status){
		this.batchName = batchName;
		this.process = process;
		this.scheduleType = scheduleType;
		this.status = status;
		
	}
	
	public BatchJobScheduling(final String batchName, final String process, String scheduleType, final String status, final Date scheduleTime, final String msgTemplateDescription){
		this.batchName = batchName;
		this.process = process;
		this.scheduleType = scheduleType;
		this.status = status;
		this.scheduleTime = scheduleTime;
		this.msgTemplateDescription = msgTemplateDescription;
		
	}
	
	
	
	public String getBatchName() {
		return batchName;
	}



	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}



	public String getProcess() {
		return process;
	}



	public void setProcess(String process) {
		this.process = process;
	}



	public String getScheduleType() {
		return scheduleType;
	}



	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

	
	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getMsgTemplateDescription() {
		return msgTemplateDescription;
	}

	public void setMsgTemplateDescription(String msgTemplateDescription) {
		this.msgTemplateDescription = msgTemplateDescription;
	}

	public static BatchJobScheduling fromJson(JsonCommand command){
		try{
		String batchName = command.stringValueOfParameterNamed("batchName");
		String process = command.stringValueOfParameterNamed("process");
		String scheduleType = command.stringValueOfParameterNamed("scheduleType");
		String status = command.stringValueOfParameterNamed("status");
		String msgTemplateDescription = null;
		
		if(process.equalsIgnoreCase("Message")){
			msgTemplateDescription = command.stringValueOfParameterNamed("msgTemplateDescription");
		}else{
			msgTemplateDescription = null;
		}
		String startDateString = command.stringValueOfParameterNamed("scheduleTime");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date scheduleTime = df.parse(startDateString);
		
		if(scheduleType.equalsIgnoreCase("Recurring"))
			scheduleType = "R";
		if(scheduleType.equalsIgnoreCase("Once"))
			scheduleType =  "O";
		
		return new BatchJobScheduling(batchName, process, scheduleType, status, scheduleTime, msgTemplateDescription);
		}catch(Exception e){
			throw new PlatformDataIntegrityException(e.getMessage(), e.getMessage(), e.getMessage());
		}
	}


	
}
