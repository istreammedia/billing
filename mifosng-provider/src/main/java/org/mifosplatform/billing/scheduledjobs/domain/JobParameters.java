package org.mifosplatform.billing.scheduledjobs.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.jobs.domain.ScheduledJobDetail;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "job_parameters")
public class JobParameters extends AbstractPersistable<Long>{



	@ManyToOne
    @JoinColumn(name="job_id")
    private ScheduleJobs jobDetail;

	@Column(name ="param_name")
    private String paramName;
	
	@Column(name ="param_type")
    private String paramType;
	
	@Column(name ="param_default_value")
    private String paramDefaultValue;
	
	@Column(name ="param_value")
    private String paramValue;
	
	@Column(name ="query_values")
    private String queryValues;


	@Column(name = "is_dynamic")
	private char isDynamic;


	public JobParameters()
	{}


	public ScheduleJobs getJobDetail() {
		return jobDetail;
	}


	public String getParamName() {
		return paramName;
	}


	public String getParamType() {
		return paramType;
	}


	public String getParamDefaultValue() {
		return paramDefaultValue;
	}


	public String getParamValue() {
		return paramValue;
	}


	public String getQueryValues() {
		return queryValues;
	}


	public char isDynamic() {
		return isDynamic;
	}
	


}