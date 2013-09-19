package org.mifosplatform.billing.mediadetails.data;

public class MediaLocationData {

	private final Long languageId;
	private final String formatType;
	private final String location;

	public MediaLocationData(Long languageId, String formatType,
			String location) {
		this.languageId=languageId;
		this.formatType=formatType;
		this.location=location;
	}

	

	public Long getLanguageId() {
		return languageId;
	}

	public String getFormatType() {
		return formatType;
	}

	public String getLocation() {
		return location;
	}

}
