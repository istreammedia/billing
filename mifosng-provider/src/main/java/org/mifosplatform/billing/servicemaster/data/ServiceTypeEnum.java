package org.mifosplatform.billing.servicemaster.data;



public enum ServiceTypeEnum {

	TV(100, "ServiceType.tv"), //
	BB(200, "ServiceType.bb"),
	VOIP(300, "ServiceType.void"),
	IPTV(400, "ServiceType.iptv"),
	VOD(500,"ServiceType.vod"),
	NONE(600,"ServiceType.none"),
	INVALID(700,"ServiceType.invalid");
	
    private final Integer value;
	private final String code;

    private ServiceTypeEnum(final Integer value, final String code) {
        this.value = value;
		this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

	public String getCode() {
		return code;
	}

	public static ServiceTypeEnum fromInt(final Integer frequency) {

		ServiceTypeEnum serviceTypeEnum = ServiceTypeEnum.INVALID;
		switch (frequency) {
		case 100:
			serviceTypeEnum= ServiceTypeEnum.TV;
			break;
		case 200:
			serviceTypeEnum= ServiceTypeEnum.BB;
			break;

		case 300:
			serviceTypeEnum= ServiceTypeEnum.VOIP;
			break;
			
		case 400:
			serviceTypeEnum= ServiceTypeEnum.IPTV;
			break;
			
		case 500:
			serviceTypeEnum= ServiceTypeEnum.VOD;
			break;

		case 600:
			serviceTypeEnum= ServiceTypeEnum.NONE;
			break;
			
		
		default:
			serviceTypeEnum= ServiceTypeEnum.INVALID;
			break;
		}
		return serviceTypeEnum;
	}
}
