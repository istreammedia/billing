package org.mifosplatform.billing.order.data;



import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.billing.plan.domain.VolumeTypeEnum;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class VolumeTypeEnumaration {

	public static EnumOptionData VolumeType(final int id) {
		return VolumeTypeEnum(VolumeTypeEnum.fromInt(id));
	}

	public static EnumOptionData VolumeTypeEnum(final VolumeTypeEnum type) {
		final String codePrefix = "volume.type.";
		EnumOptionData optionData = null;
		switch (type) {
		case IPTV:
			optionData = new EnumOptionData(VolumeTypeEnum.IPTV.getValue().longValue(), codePrefix + VolumeTypeEnum.IPTV.getCode(), "IPTV");
			break;
		case VOD:
			optionData = new EnumOptionData(VolumeTypeEnum.VOD.getValue().longValue(), codePrefix + VolumeTypeEnum.VOD.getCode(), "VOD");
			break;
		default:
			optionData = new EnumOptionData(StatusTypeEnum.INVALID.getValue().longValue(), StatusTypeEnum.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
