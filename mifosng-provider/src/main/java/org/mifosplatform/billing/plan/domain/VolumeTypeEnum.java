package org.mifosplatform.billing.plan.domain;


public enum VolumeTypeEnum {

	IPTV(1, "CategoryType.iptv"), //
	VOD(2, "CategoryType.vod"),
	INVALID(3, "CategoryType.invalid");


    private final Integer value;
	private final String code;

    private VolumeTypeEnum(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static VolumeTypeEnum fromInt(final Integer frequency) {

		VolumeTypeEnum volumeTypeEnum = VolumeTypeEnum.INVALID;
		switch (frequency) {
		case 1:
			volumeTypeEnum = VolumeTypeEnum.IPTV;
			break;
		case 2:
			volumeTypeEnum = VolumeTypeEnum.VOD;
			break;
		default:
			volumeTypeEnum = VolumeTypeEnum.INVALID;
			break;
		}
		return volumeTypeEnum;
	}
}
