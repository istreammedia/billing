package org.mifosplatform.billing.batchjobscheduling.data;

public class ScheduleTypeData {
	
	
	private Long id;
	private String scheduleType;
	
	public ScheduleTypeData() {
		
	}
	
	
	public ScheduleTypeData(final Long id, final String scheduleType){
		this.id = id;
		this.scheduleType = scheduleType;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}
	
	
}
