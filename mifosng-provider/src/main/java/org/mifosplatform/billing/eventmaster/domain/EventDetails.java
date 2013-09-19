/**
 * 
 */
package org.mifosplatform.billing.eventmaster.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Domain for {@link EventDetails}
 * 
 * @author pavani
 *
 */
@Entity
@Table(name = "b_event_detail")
public class EventDetails extends AbstractPersistable<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private EventMaster event;
	
	
	/*@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "eventDetails", orphanRemoval = true)
	private List<EventOrderdetials> details = new ArrayList<EventOrderdetials>();
	*/
	
	@Column(name = "media_id")
	private Long mediaId;
	
	public EventDetails(final Long mediaId) {
		this.mediaId = mediaId;
		this.event = null;
	}

	public void update (EventMaster event){
		this.event = event;
	}
	
	public void delete(EventMaster event) { 
		LocalDate date = new LocalDate();
		this.event =event;
		event.setEventEndDate(date.toDate());
	}
	
	public EventDetails(EventMaster event) {
		this.event = event;
	}
	
	public EventDetails() {
		
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
	 * @return the event
	 */
	public EventMaster getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(EventMaster event) {
		this.event = event;
	}
}
	