package org.mifosplatform.billing.item.data;

import org.mifosplatform.billing.item.domain.UnitEnumType;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class UniteTypeData {

	public static EnumOptionData UnitClassType(final int id) {
		return UnitClassType(UnitEnumType.fromInt(id));
	}

	public static EnumOptionData UnitClassType(final UnitEnumType type) {
		final String codePrefix = "deposit.interest.compounding.period.";
		EnumOptionData optionData = null;
		switch (type) {
		case METERS:
			optionData = new EnumOptionData(UnitEnumType.METERS.getValue().longValue(), codePrefix + UnitEnumType.METERS.getCode(), "METERS");
			break;
		case NUMBERS:
			optionData = new EnumOptionData(UnitEnumType.NUMBERS.getValue().longValue(), codePrefix + UnitEnumType.NUMBERS.getCode(), "NUMBERS");
			break;

		case HOURS:
			optionData = new EnumOptionData(UnitEnumType.HOURS.getValue().longValue(), codePrefix + UnitEnumType.HOURS.getCode(), "HOURS");
			break;
				
		default:
			optionData = new EnumOptionData(UnitEnumType.INVALID.getValue().longValue(), UnitEnumType.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
