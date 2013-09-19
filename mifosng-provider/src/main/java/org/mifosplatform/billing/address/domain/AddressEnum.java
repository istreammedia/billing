package org.mifosplatform.billing.address.domain;

public enum AddressEnum {

	PRIMARY(1, "CategoryType.primary"), //
	BILLING(2, "CategoryType.billing"),
	INVALID(3, "CategoryType.invalid");


    private final Integer value;
	private final String code;

    private AddressEnum(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static AddressEnum fromInt(final Integer frequency) {

		AddressEnum addressEnum = AddressEnum.INVALID;
		switch (frequency) {
		case 1:
			addressEnum = AddressEnum.PRIMARY;
			break;
		case 2:
			addressEnum = AddressEnum.BILLING;
			break;
		default:
			addressEnum = AddressEnum.INVALID;
			break;
		}
		return addressEnum;
	}
}
