package org.mifosplatform.billing.scheduledjobs.data;

public class ScheduleJobData {
	
	private final Long id;
	private final String processType;
	private final String query;
	private String processParam;

	public ScheduleJobData(Long id, String processType, String query, String processParam) {
          this.id=id;
          this.processType=processType;
          this.query=query;
          this.processParam=processParam;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the processType
	 */
	public String getProcessType() {
		return processType;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @return the processParam
	 */
	public String getProcessParam() {
		return processParam;
	}

	
}
