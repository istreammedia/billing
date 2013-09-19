package org.mifosplatform.billing.item.domain;
public enum UnitEnumType {

		METERS(1, "CategoryType.meters"), //
		NUMBERS(2, "CategoryType.numbers"),
        HOURS(3, "CategoryType.hours"),
		INVALID(4, "CategoryType.invalid");


	    private final Integer value;
		private final String code;

	    private UnitEnumType(final Integer value, final String code) {
	        this.value = value;
			this.code = code;
	    }

	    public Integer getValue() {
	        return this.value;
	    }

		public String getCode() {
			return code;
		}

		public static UnitEnumType fromInt(final Integer frequency) {

			UnitEnumType enumType = UnitEnumType.INVALID;
			switch (frequency) {
			case 1:
				enumType = UnitEnumType.METERS;
				break;
			case 2:
				enumType = UnitEnumType.NUMBERS;
				break;

			case 3:
				enumType = UnitEnumType.HOURS;
				break;
				default:
				enumType = UnitEnumType.INVALID;
				break;
			}
			return enumType;
		}
	}



