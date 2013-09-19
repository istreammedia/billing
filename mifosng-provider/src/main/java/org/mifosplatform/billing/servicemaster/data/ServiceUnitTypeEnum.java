package org.mifosplatform.billing.servicemaster.data;



public enum ServiceUnitTypeEnum {

	ON_OFF(1, "ServiceUnitType.onoff"), //
	SCHEME(2, "ServiceUnitType.scheme"),
	QUANTITY(3, "ServiceUnitType.quantity"),
	INVALID(4,"ServiceUnitType.invalid");
	
    private final Integer value;
	private final String code;

    private ServiceUnitTypeEnum(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	@SuppressWarnings("static-access")
	public static ServiceUnitTypeEnum fromInt(final Integer frequency) {

		ServiceUnitTypeEnum serviceUnitTypeEnum = ServiceUnitTypeEnum.INVALID;
		switch (frequency) {
		case 1:
			serviceUnitTypeEnum= serviceUnitTypeEnum.ON_OFF;
			break;
		case 2:
			serviceUnitTypeEnum= serviceUnitTypeEnum.SCHEME;
			break;

		case 3:
			serviceUnitTypeEnum= serviceUnitTypeEnum.QUANTITY  ;
			break;
		
		default:
			serviceUnitTypeEnum= serviceUnitTypeEnum.INVALID;
			break;
		}
		return serviceUnitTypeEnum;
	}
}
