package org.mifosplatform.billing.eventmaster.domain;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

/**
 * Class for {@link OptType} {@link EnumOptionData}
 * 
 * @author pavani
 *
 */
public class OptTypeEnum {

	public static EnumOptionData optType(final OptType type) {
		final String codePreFix = "optType.";
		EnumOptionData optionData = null;
		switch (type) {
		case RENT:
			optionData = new EnumOptionData(OptType.RENT.getValue().longValue(), codePreFix+OptType.RENT.getCode(), "RENT");			
			break;
		case OWN:
			optionData = new EnumOptionData(OptType.OWN.getValue().longValue(), codePreFix+OptType.OWN.getCode(), "OWN");
			break;
		default:
			optionData = new EnumOptionData(OptType.INVALID.getValue().longValue(),codePreFix+OptType.INVALID.getCode(),"INVALID");
			break;
		}
		return optionData;
	}
}
