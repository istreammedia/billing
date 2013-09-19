/**
 * 
 */
package org.mifosplatform.billing.mediadetails.data;

/**
 * @author pavani
 *
 */
public class MediaAssetLocationDetails {

	private Long id;
	private Long mediaId;
	private Integer languageId;
	private String formatType;
	private String Location;
	
	
	public MediaAssetLocationDetails(Long id, Long mediaId, Integer languageId,
									 String formatType, String locatation) {
		this.id = id;
		this.mediaId = mediaId;
		this.languageId = languageId;
		this.formatType = formatType;
		this.Location = locatation;
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the mediaId
	 */
	public Long getMediaId() {
		return mediaId;
	}


	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}


	/**
	 * @return the languageId
	 */
	public Integer getLanguageId() {
		return languageId;
	}


	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}


	/**
	 * @return the formatType
	 */
	public String getFormatType() {
		return formatType;
	}


	/**
	 * @param formatType the formatType to set
	 */
	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}


	/**
	 * @return the location
	 */
	public String getLocation() {
		return Location;
	}


	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		Location = location;
	}

}
