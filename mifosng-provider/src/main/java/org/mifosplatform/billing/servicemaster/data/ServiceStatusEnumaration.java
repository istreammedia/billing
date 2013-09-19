package org.mifosplatform.billing.servicemaster.data;



import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class ServiceStatusEnumaration {

	public static EnumOptionData serviceType(final int id) {
		return serviceType(ServiceTypeEnum.fromInt(id));
	}

	public static EnumOptionData serviceType(final ServiceTypeEnum type) {
		final String codePrefix = "service.type.";
		EnumOptionData optionData = null;
		switch (type) {
		case TV:
			optionData = new EnumOptionData(ServiceTypeEnum.TV.getValue().longValue(), codePrefix + ServiceTypeEnum.TV.getCode(), "TV");
			break;
		case BB:
			optionData = new EnumOptionData(ServiceTypeEnum.BB.getValue().longValue(), codePrefix + ServiceTypeEnum.BB.getCode(), "BB");
			break;

		case VOIP:
			optionData = new EnumOptionData(ServiceTypeEnum.VOIP.getValue().longValue(), codePrefix + ServiceTypeEnum.VOIP.getCode(), "VOIP");
			break;
			
		case IPTV:
			optionData = new EnumOptionData(ServiceTypeEnum.IPTV.getValue().longValue(), codePrefix + ServiceTypeEnum.IPTV.getCode(), "IPTV");
			break;
			
		case VOD:
			optionData = new EnumOptionData(ServiceTypeEnum.VOD.getValue().longValue(), codePrefix + ServiceTypeEnum.VOD.getCode(), "VOD");
			break;
			
		case NONE:
			optionData =new EnumOptionData(ServiceTypeEnum.NONE.getValue().longValue(),codePrefix+ServiceTypeEnum.NONE,"NONE");
			break;
		
		default:
			optionData = new EnumOptionData(ServiceTypeEnum.INVALID.getValue().longValue(), ServiceTypeEnum.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
