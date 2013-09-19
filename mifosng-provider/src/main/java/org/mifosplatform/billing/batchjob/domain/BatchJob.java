package org.mifosplatform.billing.batchjob.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Table(name="b_batch")
public class BatchJob extends AbstractPersistable<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="batch_name", nullable=false, length=20)
	private String batchName;
	
	@Column(name="query", nullable=false)
	private String query;

	public BatchJob() {
	
	}
	
	public BatchJob(final String batchName, final String query){
		this.batchName = batchName;
		this.query = query;
	}
	
	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public static BatchJob fromJson(JsonCommand command){
		String batchName = command.stringValueOfParameterNamed("batchName");
		String query = command.stringValueOfParameterNamed("query");
		return new BatchJob(batchName,query);
	}
	
}
