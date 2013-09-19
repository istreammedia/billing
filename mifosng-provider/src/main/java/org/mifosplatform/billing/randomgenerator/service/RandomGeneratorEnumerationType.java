package org.mifosplatform.billing.randomgenerator.service;

import org.mifosplatform.billing.randomgenerator.domain.PinType;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class RandomGeneratorEnumerationType {

	public static EnumOptionData enumOptionData(final int id) {
		return enumOptionData(PinType.fromInt(id));
	}

	public static EnumOptionData enumOptionData(PinType pin) {
		
		final String codePrefix = "deposit.interest.compounding.period.";
		EnumOptionData optionData = null;
		switch (pin) {
		case VALUE:
			optionData = new EnumOptionData(PinType.VALUE.getValue().longValue(), codePrefix + PinType.VALUE.getCode(), "VALUE");
			break;
		case DURATION:
			optionData = new EnumOptionData(PinType.DURATION.getValue().longValue(), codePrefix + PinType.DURATION.getCode(), "DURATION");
			break;
		/*case PRODUCTION:
			optionData = new EnumOptionData(PinType.PRODUCTION.getValue().longValue(), codePrefix + PinType.PRODUCTION.getCode(), "PRODUCTION");
			break;*/
		default:
			optionData = new EnumOptionData(PinType.INVALID.getValue().longValue(), PinType.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	
	}
	
}
