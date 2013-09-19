package org.mifosplatform.billing.plan.service;


public enum ChargeVariant {

	SUBSCRIBER_TYPE(0, "ChargeVariant.subscriber"), //
	AREAS(1, "ChargeVariant.areas"), //
	BASE(2, "ChargeVariant.base"),//
   INVALID(3, "ChargeVariant.invalid");

    private final Integer value;
	private final String code;

    private ChargeVariant(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static ChargeVariant fromInt(final Integer frequency) {

		ChargeVariant repaymentFrequencyType = ChargeVariant.INVALID;
		switch (frequency) {
		case 0:
			repaymentFrequencyType = ChargeVariant.SUBSCRIBER_TYPE;
			break;
		case 1:
			repaymentFrequencyType = ChargeVariant.AREAS;
			break;
		case 2:
			repaymentFrequencyType = ChargeVariant.BASE;
			break;

		default:
			repaymentFrequencyType = ChargeVariant.INVALID;
			break;
		}
		return repaymentFrequencyType;
	}

}
