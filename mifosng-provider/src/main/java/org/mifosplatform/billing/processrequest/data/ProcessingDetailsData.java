package org.mifosplatform.billing.processrequest.data;

public class ProcessingDetailsData {
	
	
	private final Long id;
	private final Long orderId;
	private final String provisionigSystem;
	private final String requestType;

	

	public ProcessingDetailsData(Long id, Long orderId, String provisionigSys, String requestType) {

            this.id=id;
            this.orderId=orderId;
            this.provisionigSystem=provisionigSys;
            this.requestType=requestType;
	}



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}



	/**
	 * @return the orderId
	 */
	public Long getOrderId() {
		return orderId;
	}



	/**
	 * @return the provisionigSystem
	 */
	public String getProvisionigSystem() {
		return provisionigSystem;
	}



	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}

}
