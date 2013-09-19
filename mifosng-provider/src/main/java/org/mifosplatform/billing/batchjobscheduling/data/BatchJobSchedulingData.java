package org.mifosplatform.billing.batchjobscheduling.data;

import java.util.List;

import org.joda.time.DateTime;

public class BatchJobSchedulingData {

	private Long id;
	private String batchName;
	private String process;
	private String scheduleType;
	private String status;
	private String msgTemplateDescription;
	private DateTime scheduleTime;
	
	
	private List<ProcessTypeData> processTypeData;
	private List<BatchNameData> batchNameData;
	private List<ScheduleTypeData> scheduleTypeData;
	private List<MessageTemplateData> msgTemplateData;
		
	
	public BatchJobSchedulingData() {
		
	}
	
	public BatchJobSchedulingData(final List<BatchNameData> batchNameData, final List<ProcessTypeData> procesTypeData, final List<ScheduleTypeData> scheduleTypeData, final List<MessageTemplateData> msgTemplateData){
		this.batchNameData = batchNameData;
		this.processTypeData = procesTypeData;
		this.scheduleTypeData = scheduleTypeData;
		this.msgTemplateData = msgTemplateData;
	}
	
	public BatchJobSchedulingData(final Long id, final String process){
		this.id = id;
		this.process = process;
	}
	
	public BatchJobSchedulingData(final String batchName, final String process, final String scheduleType, final String status){
		this.batchName = batchName;
		this.process = process;
		this.scheduleType = scheduleType;
		this.status = status;
	}
	
	public BatchJobSchedulingData(final Long id, final String batchName, final String process, final String scheduleType, final String status){
		this.id = id;
		this.batchName = batchName;
		this.process = process;
		this.scheduleType = scheduleType;
		this.status = status;
	}
	
	public BatchJobSchedulingData(final Long id, final String batchName, final String process, final String scheduleType, final String status, final DateTime scheduleTime, final String msgTemplateDescription){
		this.id = id;
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
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ProcessTypeData> getProcessTypeData() {
		return processTypeData;
	}

	public void setProcessTypeData(List<ProcessTypeData> processTypeData) {
		this.processTypeData = processTypeData;
	}

	public List<BatchNameData> getBatchNameData() {
		return batchNameData;
	}

	public void setBatchNameData(List<BatchNameData> batchNameData) {
		this.batchNameData = batchNameData;
	}

	public List<ScheduleTypeData> getScheduleTypeData() {
		return scheduleTypeData;
	}

	public void setScheduleTypeDatas(List<ScheduleTypeData> scheduleTypeData) {
		this.scheduleTypeData = scheduleTypeData;
	}

	public List<MessageTemplateData> getMsgTemplateData() {
		return msgTemplateData;
	}

	public void setMsgTemplateData(List<MessageTemplateData> msgTemplateData) {
		this.msgTemplateData = msgTemplateData;
	}

	public void setScheduleTypeData(List<ScheduleTypeData> scheduleTypeData) {
		this.scheduleTypeData = scheduleTypeData;
	}

	public String getMsgTemplateDescription() {
		return msgTemplateDescription;
	}

	public void setMsgTemplateDescription(String msgTemplateDescription) {
		this.msgTemplateDescription = msgTemplateDescription;
	}

	public DateTime getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(DateTime scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	

	
}
