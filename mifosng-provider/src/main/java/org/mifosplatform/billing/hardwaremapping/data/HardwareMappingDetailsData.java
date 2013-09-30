package org.mifosplatform.billing.hardwaremapping.data;

public class HardwareMappingDetailsData {

	private final Long planId;
	private final Long orderId;
	private final String planCode;

	public HardwareMappingDetailsData(Long planId, Long orderId, String planCode) {
	
		this.planId=planId;
		this.planCode=planCode;
		this.orderId=orderId;
	}

	public Long getPlanId() {
		return planId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public String getPlanCode() {
		return planCode;
	}

	
}
