package org.mifosplatform.billing.scheduledjobs.data;

import java.util.List;

public class ScheduleJobData {
	
	private final Long id;
	private final String batchName;
	private final String query;
	private List<ScheduleJobData> jobDetailData;
	

	public ScheduleJobData(Long id, String batchName, String query) {
          this.id=id;
          this.batchName=batchName;
          this.query=query;
          
	}

	public ScheduleJobData(List<ScheduleJobData> jobDetailData) {
	this.jobDetailData=jobDetailData;
	  this.id=null;
      this.batchName=null;
      this.query=null;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	public String getBatchName() {
		return batchName;
	}

	public String getQuery() {
		return query;
	}

	/**
	 * @return the processType
	 */
	
	
}
