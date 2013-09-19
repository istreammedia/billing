package org.mifosplatform.billing.servicemaster.data;



import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class ServiceUnitTypeEnumaration {

	public static EnumOptionData serviceUnitType(final int id) {
		return serviceUnitType(ServiceUnitTypeEnum.fromInt(id));
	}

	public static EnumOptionData serviceUnitType(final ServiceUnitTypeEnum type) {
		final String codePrefix = "service.unit.type.";
		EnumOptionData optionData = null;
		switch (type) {
		case ON_OFF:
			optionData = new EnumOptionData(ServiceUnitTypeEnum.ON_OFF.getValue().longValue(), codePrefix + ServiceUnitTypeEnum.ON_OFF.getCode(), "ON_OFF");
			break;
		case SCHEME:
			optionData = new EnumOptionData(ServiceUnitTypeEnum.SCHEME.getValue().longValue(), codePrefix + ServiceUnitTypeEnum.SCHEME.getCode(), "SCHEME");
			break;

		case QUANTITY:
			optionData = new EnumOptionData(ServiceUnitTypeEnum.QUANTITY.getValue().longValue(), codePrefix + ServiceUnitTypeEnum.QUANTITY.getCode(), "QUANTITY");
			break;
		
		default:
			optionData = new EnumOptionData(ServiceUnitTypeEnum.INVALID.getValue().longValue(), ServiceUnitTypeEnum.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
