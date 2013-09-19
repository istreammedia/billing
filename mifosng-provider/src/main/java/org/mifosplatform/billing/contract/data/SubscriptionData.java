package org.mifosplatform.billing.contract.data;

import java.util.List;

import org.mifosplatform.billing.contract.data.PeriodData;

public class SubscriptionData {

	private final Long id;

	private final String subscription_period;
	private final Long subscriptiontypeId;
 private final String Contractdata;
	private final String subscription_type;
	private List<PeriodData> allowedperiods;

	private final Long units;
	private String day_name;

	public SubscriptionData(final Long id, final String subscription_period,
			final String subscription_type, final Long units,final Long subscriptiontypeid,final String day_name) {

		this.id = id;
		this.subscription_period = subscription_period;
		this.subscription_type = subscription_type;
		this.units = units;
		this.subscriptiontypeId=subscriptiontypeid;
		this.day_name=day_name;
		this.Contractdata=null;
	}

	public SubscriptionData(final Long id,final String data)
	{
		this.Contractdata=data;
		this.id=id;
		this.subscription_period=null;
		this.subscription_type=null;
		this.units=null;
		this.subscriptiontypeId=null;
		this.day_name=null;

	}
	public String getDay_name() {
		return day_name;
	}

	public void setDay_name(String day_name) {
		this.day_name = day_name;
	}

	public SubscriptionData(List<PeriodData> datas, SubscriptionData products) {
		this.id = products.getId();
		this.allowedperiods = datas;
		this.subscription_period = products.getSubscription_period();
		this.units = products.getUnits();
		this.subscription_type = products.getSubscription_type();
        this.subscriptiontypeId=products.getSubscriptiontypeId();
        this.Contractdata=null;
	}

	public SubscriptionData(List<PeriodData> allowedtypes) {
		this.allowedperiods = allowedtypes;
		this.id=null;
		this.subscription_period=null;
		this.subscription_type=null;
		this.units=null;
		this.subscriptiontypeId=null;
		this.Contractdata=null;
	}

	public List<PeriodData> getAllowedperiods() {
		return allowedperiods;
	}

	public void setAllowedperiods(List<PeriodData> allowedperiods) {
		this.allowedperiods = allowedperiods;
	}

	public Long getId() {
		return id;
	}

	public String getSubscription_period() {
		return subscription_period;
	}

	public String getSubscription_type() {
		return subscription_type;
	}

	public Long getUnits() {
		return units;
	}

	public Long getSubscriptiontypeId() {
		return subscriptiontypeId;
	}

	public String getContractdata() {
		return Contractdata;
	}


}
