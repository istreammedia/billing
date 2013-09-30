package org.mifosplatform.billing.entitlements.data;

public class EntitlementsData {

	private final Long id;
	private final String userName;
	private final String planName;
	private final Long prepareReqId;
	private String requestType;
	private String hardwareId;
	private String provisioingSystem;

	public EntitlementsData(Long id, String userName, String planName, Long prepareReqId, String requestType, String hardwareId, String provisioingSystem) {
          this.id=id;
          this.prepareReqId=prepareReqId;
          this.userName=userName;
          this.planName=planName;
          this.requestType=requestType;
          this.hardwareId=hardwareId;
          this.provisioingSystem=provisioingSystem;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getPlanName() {
		return planName;
	}

	public Long getPrepareReqId() {
		return prepareReqId;
	}

	public String getRequestType() {
		return requestType;
	}

	public String getHardwareId() {
		return hardwareId;
	}

	public String getProvisioingSystem() {
		return provisioingSystem;
	}
	
	

	
}
