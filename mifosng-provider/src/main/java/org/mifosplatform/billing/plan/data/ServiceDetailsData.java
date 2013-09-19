package org.mifosplatform.billing.plan.data;

public class ServiceDetailsData {

	private final Long id;
	private final String service_code;
	private final String plan_code;

	public ServiceDetailsData(final Long id,final String service_code,final String plan_code)
	{

		this.id=id;
		this.service_code=service_code;
		this.plan_code=plan_code;
	}

	public Long getId() {
		return id;
	}

	public String getService_code() {
		return service_code;
	}

	public String getPlan_code() {
		return plan_code;
	}

}
