package org.mifosplatform.billing.scheduledjobs.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_schedule_jobs")
public class ScheduleJobs extends AbstractPersistable<Long>{


	@Column(name = "batch_name")
	private String batchName;

	@Column(name = "process")
	private String processType;

	@Column(name = "schedule_type")
	private char bactchType;

	@Column(name = "status")
	private char status;
	
	
	 public  ScheduleJobs() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the batchName
	 */
	public String getBatchName() {
		return batchName;
	}


	/**
	 * @return the processType
	 */
	public String getProcessType() {
		return processType;
	}


	/**
	 * @return the bactchType
	 */
	public char getBactchType() {
		return bactchType;
	}


	/**
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(char status) {
		this.status = status;
	}


	

	


	
	

		
	 
	 
			
	}
 
	
	
