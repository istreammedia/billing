package org.mifosplatform.billing.randomgenerator.domain;

public enum PinCategory {

	   NUMERIC(1, "PinCategory.numeric"), //
	   ALPHA(2, "PinCategory.alpha"),//
	   ALPHANUMERIC(3,"CategoryType.alphanumeric"),//
	   INVALID(4, "CategoryType.invalid");

	    private final Integer value;
		private final String code;

	    private PinCategory(final Integer value, final String code) {
	        this.value = value;
			this.code = code;
	    }

	    public Integer getValue() {
	        return this.value;
	    }

		public String getCode() {
			return code;
		}

		public static PinCategory fromInt(final Integer category) {

			PinCategory pinCategory = PinCategory.INVALID;
			switch (category) {
			case 1:
				pinCategory = PinCategory.NUMERIC;
				break;
			case 2:
				pinCategory = PinCategory.ALPHA;
				break;
			case 3:
				pinCategory = PinCategory.ALPHANUMERIC;
				break;

			default:
				pinCategory = PinCategory.INVALID;
				break;
			}
			return pinCategory;
		}
	}
