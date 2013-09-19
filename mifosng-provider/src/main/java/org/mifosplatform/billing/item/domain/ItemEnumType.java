package org.mifosplatform.billing.item.domain;
public enum ItemEnumType {

		HARDWARE(1, "CategoryType.Hardware"), //
		SOFT_CHARGE(2, "CategoryType.Soft Charge"),
        PREPAID_CARD(3, "CategoryType.prepaid Card"),
		EVENT(4, "CategoryType.Event"),
		INVALID(5, "CategoryType.invalid");


	    private final Integer value;
		private final String code;

	    private ItemEnumType(final Integer value, final String code) {
	        this.value = value;
			this.code = code;
	    }

	    public Integer getValue() {
	        return this.value;
	    }

		public String getCode() {
			return code;
		}

		public static ItemEnumType fromInt(final Integer frequency) {

			ItemEnumType enumType = ItemEnumType.INVALID;
			switch (frequency) {
			case 1:
				enumType = ItemEnumType.HARDWARE;
				break;
			case 2:
				enumType = ItemEnumType.SOFT_CHARGE;
				break;

			case 3:
				enumType = ItemEnumType.PREPAID_CARD;
				break;
			case 4:
				enumType = ItemEnumType.EVENT;
				break;
				default:
				enumType = ItemEnumType.INVALID;
				break;
			}
			return enumType;
		}
	}



