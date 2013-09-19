package org.mifosplatform.billing.ticketmaster.data;

public class ProblemsData {
	
	private final Long problemCode;
	private final String problemDescription;
	private final String satus;
	private final Integer statusCode;
	
	
	public ProblemsData(final Long problemCode,final String problemDesc, final String status,
						final Integer statusCode){
		this.problemCode=problemCode;
		this.problemDescription=problemDesc;
		this.satus = status;
		this.statusCode = statusCode;
	}

	/**
	 * @return the problemCode
	 */
	public Long getProblemCode() {
		return problemCode;
	}

	/**
	 * @return the problemDescription
	 */
	public String getProblemDescription() {
		return problemDescription;
	}

	/**
	 * @return the satus
	 */
	public String getSatus() {
		return satus;
	}

	/**
	 * @return the statusCode
	 */
	public Integer getStatusCode() {
		return statusCode;
	}
	
	
}
