package org.mifosplatform.billing.media.domain;



import org.mifosplatform.billing.address.domain.AddressEnum;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.data.MediaEnumoptionData;

public class MediaTypeEnumaration {

	public static MediaEnumoptionData enumOptionData(final int id) {
		return enumOptionData(MediaEnum.fromInt(id));
	}

	public static MediaEnumoptionData enumOptionData(final MediaEnum mediaEnum) {
		final String codePrefix = "deposit.interest.compounding.period.";
		MediaEnumoptionData optionData = null;
		switch (mediaEnum) {
		case MOVIES:
			optionData = new MediaEnumoptionData(MediaEnum.MOVIES.getValue(), codePrefix + MediaEnum.MOVIES.getCode(), "MOVIES");
			break;
		case TV_SERIALS:
			optionData = new MediaEnumoptionData(MediaEnum.TV_SERIALS.getValue(), codePrefix + MediaEnum.TV_SERIALS.getCode(), "TV SERIALS");
			break;
		case COMING_SOON:
			optionData = new MediaEnumoptionData(MediaEnum.COMING_SOON.getValue(), codePrefix + MediaEnum.COMING_SOON.getCode(), "COMING SOON");
			break;
		default:
			optionData = new MediaEnumoptionData(MediaEnum.INVALID.getValue(), codePrefix + MediaEnum.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
