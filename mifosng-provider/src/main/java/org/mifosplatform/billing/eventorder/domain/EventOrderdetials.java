/**
 * 
 */
package org.mifosplatform.billing.eventorder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.mifosplatform.billing.eventmaster.domain.EventDetails;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_eventorder_details")
public class EventOrderdetials extends AbstractPersistable<Long> {

	
	@ManyToOne
	@JoinColumn(name = "eventorder_id")
	private EventOrder eventOrder;
	
	@OneToOne
	@JoinColumn(name = "eventdetail_id")
	private EventDetails eventDetails;
	
	@Column(name = "movie_link")
	private String movieLink;
	
	
	@Column(name = "format_type")
	private String formatType;
	
	@Column(name = "opt_type")
	private String optType;
	
	public EventOrderdetials(){
		
	}

	public EventOrderdetials(EventDetails eventDetail, String movieLink, String formatType, String optType) {
                  this.eventDetails=eventDetail;
                  this.movieLink=movieLink;
                  this.optType=optType;
                  this.formatType=formatType;
		
	}

	public EventOrder getEventOrder() {
		return eventOrder;
	}
	public EventDetails getEventDetails() {
		
		return eventDetails;
	}

	public String getMovieLink() {
		return movieLink;
	}

	public void update(EventOrder eventOrder) {
		this.eventOrder=eventOrder;
		
	}
	

	
}
	