package org.mifosplatform.billing.batchjob.data;

public class BatchJobData {

	
	private Long id;
	
	private String batchName;
	
	private String query;

	
	public BatchJobData() {
	
	}
	
	public BatchJobData(final Long id, final String batchName, final String query){
		this.id = id;
		this.batchName = batchName;
		this.query = query;
	}
	
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	
	
	
}
