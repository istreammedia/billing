package org.mifosplatform.billing.plan.data;

import java.util.List;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class EnumData {

	private List<EnumOptionData> statusType,billingType,billingCycleType;

	public EnumData( final List<EnumOptionData> statusType,final List<EnumOptionData> billingType,final List<EnumOptionData> billingCycleType)
	{

		this.billingType=billingType;
		this.statusType=statusType;
		this.billingCycleType=billingCycleType;
	}

	public List<EnumOptionData> getStatusType() {
		return statusType;
	}

	public List<EnumOptionData> getBillingType() {
		return billingType;
	}

	public List<EnumOptionData> getBillingCycleType() {
		return billingCycleType;
	}


}
