package org.mifosplatform.billing.planservice.data;

public class PlanServiceData {
	
	private final Long serviceId;
	private final Long clientId;
	private final String channelName;
	private final String image;
	private final String url;

	public PlanServiceData(Long serviceId, Long clientId, String serviceName,
			String logo, String url) {
           this.serviceId=serviceId;
           this.clientId=clientId;
           this.channelName=serviceName;
           this.image=logo;
           this.url=url;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public Long getClientId() {
		return clientId;
	}

	public String getServiceName() {
		return channelName;
	}

	public String getLogo() {
		return image;
	}

}
