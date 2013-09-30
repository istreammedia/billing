package org.mifosplatform.billing.plan.domain;


public enum StatusTypeEnum {

	ACTIVE(1, "CategoryType.active"), //
	INACTIVE(2, "CategoryType.inactive"),
	DISCONNECTED(3, "CategoryType.disconnected"),
	PENDING(4,"CategoryType.pending"),
	INVALID(5, "CategoryType.invalid");


    private final Integer value;
	private final String code;

    private StatusTypeEnum(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static StatusTypeEnum fromInt(final Integer frequency) {

		StatusTypeEnum repaymentFrequencyType = StatusTypeEnum.INVALID;
		switch (frequency) {
		case 1:
			repaymentFrequencyType = StatusTypeEnum.ACTIVE;
			break;
		case 2:
			repaymentFrequencyType = StatusTypeEnum.INACTIVE;
			break;

		case 3:
			repaymentFrequencyType = StatusTypeEnum.DISCONNECTED;
			break;
			
		case 4:
			repaymentFrequencyType = StatusTypeEnum.PENDING;
			break;
			
		default:
			repaymentFrequencyType = StatusTypeEnum.INVALID;
			break;
		}
		return repaymentFrequencyType;
	}
}
