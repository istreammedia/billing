/**
 * 
 */
package org.mifosplatform.billing.eventmaster.data;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.eventmaster.domain.EventDetails;

/**
 * POJO for {@link EventDetails}
 * 
 * @author pavani
 *
 */
public class EventDetailsData {
	
	Long id;
	Integer eventId;
	Long mediaId;
	LocalDate eventStartDate;
	LocalDate eventEndDate;
	String mediaTitle; 
	
	
	public EventDetailsData(Long id,Integer eventId,Long mediaId,
							LocalDate eventStartDate,LocalDate eventEndDate) {
		this.id = id;
		this.eventId = eventId;
		this.mediaId = mediaId;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
	}
	
	public EventDetailsData(Long id, Integer eventId,Long mediaId, String mediaTitle) {
		this.id= id;
		this.eventId = eventId;
		this.mediaId = mediaId;
		this.mediaTitle = mediaTitle;
	}
	
	public EventDetailsData() {
		
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
	 * @return the eventId
	 */
	public Integer getEventId() {
		return eventId;
	}
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
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
}
