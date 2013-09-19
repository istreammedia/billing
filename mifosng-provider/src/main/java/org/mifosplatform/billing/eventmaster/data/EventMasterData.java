/**
 * 
 */
package org.mifosplatform.billing.eventmaster.data;

import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.billing.item.data.ChargesData;
import org.mifosplatform.billing.media.data.MediaAssetData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

/**
 * POJO for {@link EventMaster}
 * 
 * @author pavani
 *
 */
public class EventMasterData {

	private Long id;
	private String eventName;
	private String eventDescription;
	private String status;
	private LocalDate eventStartDate;
	private LocalDate eventEndDate;
	private LocalDate eventValidity;
	private Integer createdbyId;
	private LocalDate createdDate;
	private String allowCancellation;
	private String chargeCode;
	
	private String mediaTitle;
	
	private List<EnumOptionData> statusData;
	private List<MediaAssetData> mediaAsset;
	private List<EventDetailsData> selectedMedia;
	private List<EnumOptionData> optType;
	private List<ChargesData> chargeData;
	private EventMasterData eventMasterData;
	private List<EventDetailsData> eventDetails;
	
	
	public EventMasterData() {
		
	}
	
	public EventMasterData (List<MediaAssetData> mediaAsset, List<EnumOptionData> statusData,
							List<EnumOptionData> optType, List<ChargesData> chargeDatas) {
		this.mediaAsset = mediaAsset;
		this.statusData = statusData;
		this.optType = optType;
		this.chargeData=chargeDatas;
	}
	
	public EventMasterData(Long id, String eventName, String eventDescription,
						   String status, String mediaTitle, LocalDate createdDate ) {
		this.id = id;
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.status = status;
		this.mediaTitle = mediaTitle;
		this.createdDate = createdDate;
	}
	
	public EventMasterData(Long id,String eventName,String eventDescription,
						   String status, String mediaTitle, LocalDate eventStartDate,
						   LocalDate eventEndDate,LocalDate eventValidity,Integer allowCancellation, String chargeData) {
		this.id= id;
		this.eventName= eventName;
		this.eventDescription = eventDescription;
		this.status = status;
		this.mediaTitle = mediaTitle;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.eventValidity = eventValidity;
		if(allowCancellation == 1) {
			this.allowCancellation = "true";
		} else {
			this.allowCancellation = "false";
		}
		this.chargeCode = chargeData;
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
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	/**
	 * @return the eventDescription
	 */
	public String getEventDescription() {
		return eventDescription;
	}
	/**
	 * @param eventDescription the eventDescription to set
	 */
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the eventStartDate
	 */
	public LocalDate getEventStartDate() {
		return eventStartDate;
	}
	/**
	 * @param eventStartDate the eventStartDate to set
	 */
	public void setEventStartDate(LocalDate eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	/**
	 * @return the eventEndDate
	 */
	public LocalDate getEventEndDate() {
		return eventEndDate;
	}
	/**
	 * @param eventEndDate the eventEndDate to set
	 */
	public void setEventEndDate(LocalDate eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	/**
	 * @return the eventValidity
	 */
	public LocalDate getEventValidity() {
		return eventValidity;
	}
	/**
	 * @param eventValidity the eventValidity to set
	 */
	public void setEventValidity(LocalDate eventValidity) {
		this.eventValidity = eventValidity;
	}
	/**
	 * @return the createdbyId
	 */
	public Integer getCreatedbyId() {
		return createdbyId;
	}
	/**
	 * @param createdbyId the createdbyId to set
	 */
	public void setCreatedbyId(Integer createdbyId) {
		this.createdbyId = createdbyId;
	}
	/**
	 * @return the createdDate
	 */
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the mediaAsset
	 */
	public List<MediaAssetData> getMediaAsset() {
		return mediaAsset;
	}

	/**
	 * @param mediaAsset the mediaAsset to set
	 */
	public void setMediaAsset(List<MediaAssetData> mediaAsset) {
		this.mediaAsset = mediaAsset;
	}

	/**
	 * @return the statusData
	 */
	public List<EnumOptionData> getStatusData() {
		return statusData;
	}

	/**
	 * @param statusData the statusData to set
	 */
	public void setStatusData(List<EnumOptionData> statusData) {
		this.statusData = statusData;
	}

	/**
	 * @return the mediaTitle
	 */
	public String getMediaTitle() {
		return mediaTitle;
	}

	/**
	 * @param mediaTitle the mediaTitle to set
	 */
	public void setMediaTitle(String mediaTitle) {
		this.mediaTitle = mediaTitle;
	}

	/**
	 * @return the optType
	 */
	public List<EnumOptionData> getOptType() {
		return optType;
	}

	/**
	 * @param optType the optType to set
	 */
	public void setOptType(List<EnumOptionData> optType) {
		this.optType = optType;
	}

	/**
	 * @return the eventMasterData
	 */
	public EventMasterData getEventMasterData() {
		return eventMasterData;
	}

	/**
	 * @param eventMasterData the eventMasterData to set
	 */
	public void setEventMasterData(EventMasterData eventMasterData) {
		this.eventMasterData = eventMasterData;
	}

	/**
	 * @return the eventDetails
	 */
	public List<EventDetailsData> getEventDetails() {
		return eventDetails;
	}

	/**
	 * @param eventDetails the eventDetails to set
	 */
	public void setEventDetailsData(List<EventDetailsData> eventDetails) {
		this.eventDetails = eventDetails;
	}

	/**
	 * @return the allowCanelation
	 */
	public String isAllowCancellation() {
		return allowCancellation;
	}

	/**
	 * @param allowCanelation the allowCanelation to set
	 */
	public void setAllowCancellation(String allowCancellation) {
		this.allowCancellation = allowCancellation;
	}

	/**
	 * @param eventDetails the eventDetails to set
	 */
	public void setEventDetails(List<EventDetailsData> eventDetails) {
		this.eventDetails = eventDetails;
	}

	/**
	 * @return the selectedMedia
	 */
	public List<EventDetailsData> getSelectedMedia() {
		return selectedMedia;
	}

	/**
	 * @param details the selectedMedia to set
	 */
	public void setSelectedMedia(List<EventDetailsData> details) {
		this.selectedMedia = details;
	}

	/**
	 * @return the chargeData
	 */
	public List<ChargesData> getChargeData() {
		return chargeData;
	}

	/**
	 * @param chargeData the chargeData to set
	 */
	public void setChargeData(List<ChargesData> chargeData) {
		this.chargeData = chargeData;
	}

	/**
	 * @return the chargeCode
	 */
	public String getChargeCode() {
		return chargeCode;
	}

	/**
	 * @param chargeCode the chargeCode to set
	 */
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	/**
	 * @return the allowCancellation
	 */
	public String getAllowCancellation() {
		return allowCancellation;
	}
}
