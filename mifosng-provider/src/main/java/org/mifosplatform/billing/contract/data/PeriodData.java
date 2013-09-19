package org.mifosplatform.billing.contract.data;

public class PeriodData {


	private final String subscriptionType;
	private final Long id;
	private final String dayName;

	public PeriodData(final String subscriptionType,final Long id,final String dayName)
	{
		this.subscriptionType=subscriptionType;
		this.id=id;
		this.dayName=dayName;
	}

	public String getDayName() {
		return dayName;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public Long getId() {
		return id;
	}


}
