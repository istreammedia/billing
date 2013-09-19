package org.mifosplatform.billing.plan.domain;


public enum UserActionStatusTypeEnum {

	ACTIVATION(1, "CategoryType.active"), //
	DISCONNECTION(2, "CategoryType.disconnected"),
	RECONNECTION(3,"CategoryType.reconnection"),
	INVALID(4, "CategoryType.invalid");


    private final Integer value;
	private final String code;

    private UserActionStatusTypeEnum(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static UserActionStatusTypeEnum fromInt(final Integer frequency) {

		UserActionStatusTypeEnum actionStatusTypeEnum = UserActionStatusTypeEnum.INVALID;
		switch (frequency) {
		case 1:
			actionStatusTypeEnum = UserActionStatusTypeEnum.ACTIVATION;
			break;
		case 2:
			actionStatusTypeEnum = UserActionStatusTypeEnum.DISCONNECTION;
			break;

		case 3:
			actionStatusTypeEnum = UserActionStatusTypeEnum.RECONNECTION;
			break;
		
			
		default:
			actionStatusTypeEnum = UserActionStatusTypeEnum.INVALID;
			break;
		}
		return actionStatusTypeEnum;
	}
}
