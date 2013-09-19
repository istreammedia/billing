package org.mifosplatform.billing.plan.domain;

public enum BillingTypeEnum {
	EXACT(0, "CategoryType.exact"), //
	FULL(1, "CategoryType.full"),
	CUSTOM(1, "CategoryType.custom"),
	  INVALID(3, "CategoryType.invalid");


    private final Integer value;
	private final String code;

    private BillingTypeEnum(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static BillingTypeEnum fromInt(final Integer frequency) {

		BillingTypeEnum repaymentFrequencyType = BillingTypeEnum.INVALID;
		switch (frequency) {
		case 0:
			repaymentFrequencyType = BillingTypeEnum.EXACT;
			break;
		case 1:
			repaymentFrequencyType = BillingTypeEnum.FULL;
			break;
		case 2:
			repaymentFrequencyType = BillingTypeEnum.CUSTOM;
			break;


		default:
			repaymentFrequencyType = BillingTypeEnum.INVALID;
			break;
		}
		return repaymentFrequencyType;
	}

}
