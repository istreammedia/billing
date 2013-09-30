package org.mifosplatform.billing.scheduledjobs.domain;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
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
	
	
	 @LazyCollection(LazyCollectionOption.FALSE)
		@OneToMany(cascade = CascadeType.ALL, mappedBy = "jobDetail", orphanRemoval = true)
		private List<JobParameters> details = new ArrayList<JobParameters>();
	
	@Column(name = "is_active")
	private char isActive;
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


	public void update() {
		this.isActive='Y';
		
	}


	public void updateActiveState() {
		
		if(this.isActive == 'Y'){
			this.isActive = 'N';
		}
		
	}


	public List<JobParameters> getDetails() {
		return details;
	}


	public char getIsActive() {
		return isActive;
	}

			
	}
 
	
	
