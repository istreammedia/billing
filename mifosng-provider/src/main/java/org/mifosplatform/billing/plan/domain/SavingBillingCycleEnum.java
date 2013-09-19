package org.mifosplatform.billing.plan.domain;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class SavingBillingCycleEnum {


	public static EnumOptionData interestCompoundingPeriodType(final int id) {
		return interestCompoundingPeriodType(BillingCycleType.fromInt(id));
	}

	public static EnumOptionData interestCompoundingPeriodType(final BillingCycleType type) {
		final String codePrefix = "deposit.interest.compounding.period.";
		EnumOptionData optionData = null;
		switch (type) {
		case OPTION:
			optionData = new EnumOptionData(BillingCycleType.OPTION.getValue().longValue(), codePrefix + BillingCycleType.OPTION.getCode(), "OPTION");
			break;
		case ANY_DAY:
			optionData = new EnumOptionData(BillingCycleType.ANY_DAY.getValue().longValue(), codePrefix + BillingCycleType.ANY_DAY.getCode(), "ANY_DAY");
			break;

		default:
			optionData = new EnumOptionData(BillingCycleType.INVALID.getValue().longValue(), BillingCycleType.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
