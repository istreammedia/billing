package org.mifosplatform.billing.uploadstatus.domain;

public enum UploadStatusEnum {

	NEW(1, "CategoryType.direct"), //
	COMPLETED(2, "CategoryType.cash"),
	ERROR(3, "CategoryType.cash"),
	  INVALID(4, "CategoryType.invalid");


    private final Integer value;
	private final String code;

    private UploadStatusEnum(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static UploadStatusEnum fromInt(final Integer frequency) {

		UploadStatusEnum repaymentFrequencyType = UploadStatusEnum.INVALID;
		switch (frequency) {
		case 1:
			repaymentFrequencyType = UploadStatusEnum.NEW;
			break;
		case 2:
			repaymentFrequencyType = UploadStatusEnum.COMPLETED;
			break;

		case 3:
			repaymentFrequencyType = UploadStatusEnum.ERROR;
			break;


		default:
			repaymentFrequencyType = UploadStatusEnum.INVALID;
			break;
		}
		return repaymentFrequencyType;
	}
}
