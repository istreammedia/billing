package org.mifosplatform.billing.plan.domain;

public enum BillingCycleType {


	OPTION(0, "CategoryType.exact"), //
	ANY_DAY(1, "CategoryType.full"),

	  INVALID(3, "CategoryType.invalid");


    private final Integer value;
	private final String code;

    private BillingCycleType(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static BillingCycleType fromInt(final Integer frequency) {

		BillingCycleType repaymentFrequencyType = BillingCycleType.INVALID;
		switch (frequency) {
		case 0:
			repaymentFrequencyType = BillingCycleType.OPTION;
			break;
		case 1:
			repaymentFrequencyType = BillingCycleType.ANY_DAY;
			break;

		default:
			repaymentFrequencyType = BillingCycleType.INVALID;
			break;
		}
		return repaymentFrequencyType;
	}

}
