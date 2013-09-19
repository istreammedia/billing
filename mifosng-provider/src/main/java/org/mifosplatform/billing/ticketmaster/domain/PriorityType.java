package org.mifosplatform.billing.ticketmaster.domain;


public enum PriorityType {

		LOW(1, "CategoryType.low"), //
		MEDIUM(2, "CategoryType.medium"),
		HIGH(3, "CategoryType.high"),
		  INVALID(4, "CategoryType.invalid");


	    private final Integer value;
		private final String code;

	    private PriorityType(final Integer value, final String code) {
	        this.value = value;
			this.code = code;
	    }

	    public Integer getValue() {
	        return this.value;
	    }

		public String getCode() {
			return code;
		}

		public static PriorityType fromInt(final Integer frequency) {

			PriorityType priorityType = PriorityType.INVALID;
			switch (frequency) {
			case 1:
				priorityType	 = PriorityType.LOW;
				break;
			case 2:
				priorityType = PriorityType.MEDIUM;
				break;

			case 3:
				priorityType = PriorityType.HIGH;
				break;


			default:
				priorityType = PriorityType.INVALID;
				break;
			}
			return priorityType;
		}
	}


