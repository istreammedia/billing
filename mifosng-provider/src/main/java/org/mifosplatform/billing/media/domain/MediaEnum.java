package org.mifosplatform.billing.media.domain;




public enum MediaEnum {

	 MOVIES("M", "CategoryType.movies"), //
	 TV_SERIALS("TV", "CategoryType.tvserials"),
	 COMING_SOON("CS", "CategoryType.tvserials"),
	  INVALID("N", "CategoryType.invalid");


    private final String value;
	private final String code;

    private MediaEnum(final String value, final String code) {
        this.value = value;
		this.code = code;
    }

    public String getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static MediaEnum fromInt(final Integer frequency) {

		MediaEnum addressEnum = MediaEnum.INVALID;
		switch (frequency) {
		case 1:
			addressEnum = MediaEnum.MOVIES;
			break;
		case 2:
			addressEnum = MediaEnum.TV_SERIALS;
			break;
		case 3:
			addressEnum = MediaEnum.COMING_SOON;
			break;
		default:
			addressEnum = MediaEnum.INVALID;
			break;
		}
		return addressEnum;
	}
}
