package org.mifosplatform.billing.batchjobscheduling.data;

public class BatchNameData {

	private Long id;
	private String batchName;
	
	public BatchNameData() {
		// TODO Auto-generated constructor stub
	}
	
	public BatchNameData(final Long id, final String batchName){
		this.id = id;
		this.batchName = batchName;
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

	
	
	
	
}
