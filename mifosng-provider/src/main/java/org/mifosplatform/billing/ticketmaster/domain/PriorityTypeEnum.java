package org.mifosplatform.billing.ticketmaster.domain;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class PriorityTypeEnum {
	
		public static EnumOptionData priorityType(final int id) {
			return priorityType(PriorityType.fromInt(id));
		}

		public static EnumOptionData priorityType(final PriorityType type) {
			final String codePrefix = "deposit.interest.compounding.period.";
			EnumOptionData optionData = null;
			switch (type) {
			case LOW:
				optionData = new EnumOptionData(PriorityType.LOW.getValue().longValue(), codePrefix + PriorityType.LOW.getCode(), "LOW");
				break;
			case MEDIUM:
				optionData = new EnumOptionData(PriorityType.MEDIUM.getValue().longValue(), codePrefix + PriorityType.MEDIUM.getCode(), "MEDIUM");
				break;

			case HIGH:
				optionData = new EnumOptionData(PriorityType.HIGH.getValue().longValue(), codePrefix + PriorityType.HIGH.getCode(), "HIGH");
				break;

			default:
				optionData = new EnumOptionData(PriorityType.INVALID.getValue().longValue(), PriorityType.INVALID.getCode(), "INVALID");
				break;
			}
			return optionData;
		}

	}



