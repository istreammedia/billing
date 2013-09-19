package org.mifosplatform.billing.randomgenerator.domain;

public enum PinType {

	   VALUE(1, "PinType.value"), //
	   DURATION(2, "PinType.duration"),//
	   //PRODUCTION(3,"PinType.production"),//
	   INVALID(3, "PinType.invalid");

	    private final Integer value;
		private final String code;

	    private PinType(final Integer value, final String code) {
	        this.value = value;
			this.code = code;
	    }

	    public Integer getValue() {
	        return this.value;
	    }

		public String getCode() {
			return code;
		}

		public static PinType fromInt(final Integer type) {

			PinType pinType= PinType.INVALID;
			switch (type) {
			case 1:
				pinType = PinType.VALUE;
				break;
			case 2:
				pinType = PinType.DURATION;
				break;
			/*case 3:
				pinType = PinType.PRODUCTION;
				break;
*/
			default:
				pinType = PinType.INVALID;
				break;
			}
			return pinType;
		}
	
}
