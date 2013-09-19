package org.mifosplatform.billing.eventorder.domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.joda.time.LocalDate;
import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.billing.eventpricing.domain.EventPricing;
import org.mifosplatform.billing.media.exceptions.NoPricesFoundException;
import org.mifosplatform.billing.mediadevice.data.MediaDeviceData;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;

@Entity
@Table(name = "b_eventorder")
public class EventOrder extends AbstractAuditableCustom<AppUser, Long> {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "event_id")
	private Long eventId;

	@Column(name = "eventprice_id")
	private Long eventpriceId;


	@Column(name = "booked_price")
	private Double bookedPrice;

	@Column(name = "event_bookeddate")
	private Date eventBookedDate;

	@Column(name = "event_validtill")
	private Date eventValidtill;

	@Column(name = "event_status")
	private int eventStatus;

	@Column(name = "charge_code")
	private String chargeCode;

	@Column(name = "cancel_flag")
	private char cancelFlag;
  
	@Column(name="is_invoiced")
	private char isInvoiced='N';
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL , mappedBy = "eventOrder" , orphanRemoval = true)
	private List<EventOrderdetials> eventOrderdetials = new ArrayList<EventOrderdetials>();
	

	 public EventOrder() {
		// TODO Auto-generated constructor stub
			
	}

	public EventOrder(Long eventId, LocalDate eventBookedDate,Date eventValidtill, Long eventPriceId, Double bookedPrice,
			Long clientId, int status, String chargeCode) {

	       this.eventId=eventId;
	       this.eventBookedDate=eventBookedDate.toDate();
	       this.eventValidtill=eventValidtill;
	       this.bookedPrice=bookedPrice;
	       this.clientId=clientId;
	      // this.movieLink=movieLink;
	       this.eventpriceId=eventPriceId;
	       this.eventStatus=status;
	       this.chargeCode=chargeCode;
	}

	public static EventOrder fromJson(JsonCommand command, EventMaster eventMaster, MediaDeviceData details) {
		 final Long eventId = command.longValueOfParameterNamed("eventId");
		 final Long clientId = details.getClientId();//command.longValueOfParameterNamed("clientId");
		 final LocalDate eventBookedDate=command.localDateValueOfParameterNamed("eventBookedDate");
		 final Long clientType=details.getClientTypeId();//command.integerValueOfParameterNamed("clientType");
		 final String optType=command.stringValueOfParameterNamed("optType");
		 final String formatType=command.stringValueOfParameterNamed("formatType");
		 final Date eventValidtill=eventMaster.getEventValidity();
		 final Long eventPriceId=eventMaster.getEventPricings().get(0).getId();
		 Double bookedPrice=null;
		 List<EventPricing> eventPricings=eventMaster.getEventPricings();
		 
		 for(EventPricing eventPricing:eventPricings){
			 if(eventPricing.getClientType()==clientType && eventPricing.getFormatType().equalsIgnoreCase(formatType)
					 && eventPricing.getOptType().equalsIgnoreCase(optType)){
				   bookedPrice=eventPricing.getPrice();
			 }
		 }
		if(bookedPrice == null){
			
				 throw new NoPricesFoundException();
			
		}
			
		 final int status=eventMaster.getStatus();
		 final String chargeCode=eventMaster.getChargeCode();
		 
		 return new EventOrder(eventId,eventBookedDate,eventValidtill,eventPriceId,bookedPrice,clientId,status,chargeCode);
	}

	public Long getClientId() {
		return clientId;
	}

	public Long getEventId() {
		return eventId;
	}

	public Long getEventpriceId() {
		return eventpriceId;
	}



	public Double getBookedPrice() {
		return bookedPrice;
	}

	public Date getEventBookedDate() {
		return eventBookedDate;
	}

	public Date getEventValidtill() {
		return eventValidtill;
	}

	public int getEventStatus() {
		return eventStatus;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public char getCancelFlag() {
		return cancelFlag;
	}

	

	public void setInvoiced() {
		this.isInvoiced='Y';
		
	}

	public void addEventOrderDetails(EventOrderdetials eventOrderdetials) {
             eventOrderdetials.update(this);
             this.eventOrderdetials.add(eventOrderdetials);
		
	}

	public char getIsInvoiced() {
		return isInvoiced;
	}

	public List<EventOrderdetials> getEventOrderdetials() {
		return eventOrderdetials;
	}

	
	
}
