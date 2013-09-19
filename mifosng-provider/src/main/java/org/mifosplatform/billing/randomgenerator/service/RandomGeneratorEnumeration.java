package org.mifosplatform.billing.randomgenerator.service;


import org.mifosplatform.billing.randomgenerator.domain.PinCategory;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class RandomGeneratorEnumeration {
	
	public static EnumOptionData enumOptionData(final int id) {
		return enumOptionData(PinCategory.fromInt(id));
	}

	public static EnumOptionData enumOptionData(PinCategory pin) {
		
		final String codePrefix = "deposit.interest.compounding.period.";
		EnumOptionData optionData = null;
		switch (pin) {
		case NUMERIC:
			optionData = new EnumOptionData(PinCategory.NUMERIC.getValue().longValue(), codePrefix + PinCategory.NUMERIC.getCode(), "NUMERIC");
			break;
		case ALPHA:
			optionData = new EnumOptionData(PinCategory.ALPHA.getValue().longValue(), codePrefix + PinCategory.ALPHA.getCode(), "ALPHA");
			break;
		case ALPHANUMERIC:
			optionData = new EnumOptionData(PinCategory.ALPHANUMERIC.getValue().longValue(), codePrefix + PinCategory.ALPHANUMERIC.getCode(), "ALPHANUMERIC");
			break;
		default:
			optionData = new EnumOptionData(PinCategory.INVALID.getValue().longValue(), PinCategory.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	
	}

}
