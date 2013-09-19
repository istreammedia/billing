package org.mifosplatform.billing.preparerequest.data;

public class PrepareRequestData {
	private final Long requestId;
	private final Long clientId;
	private final Long orderId;
	private final String requestType;
	private final String hardwareId;
	private final String userName;
	private final String provisioningSystem;
	private final String planName;
	private final String ishardwareReq;

	

	
	public PrepareRequestData(Long id, Long clientId, Long orderId,
			String requestType, String hardWareId, String userName,
			String provisioningSys, String planName, String ishwReq) {
		      this.requestId=id;
		      this.clientId=clientId;
		      this.orderId=orderId;
		      this.requestType=requestType;
		      this.hardwareId=hardWareId;
		      this.userName=userName;
		      this.provisioningSystem=provisioningSys;
		      this.planName=planName;
		      this.ishardwareReq=ishwReq;
		      
		
	}


	public Long getRequestId() {
		return requestId;
	}


	public Long getClientId() {
		return clientId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public String getRequestType() {
		return requestType;
	}

	

	public String getHardwareId() {
		return hardwareId;
	}


	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}


	/**
	 * @return the provisioningSystem
	 */
	public String getProvisioningSystem() {
		return provisioningSystem;
	}


	/**
	 * @return the planName
	 */
	public String getPlanName() {
		return planName;
	}


	/**
	 * @return the ishardwareReq
	 */
	public String getIshardwareReq() {
		return ishardwareReq;
	}
	
	

}
