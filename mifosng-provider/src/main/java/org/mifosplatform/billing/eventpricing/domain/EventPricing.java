package org.mifosplatform.billing.eventpricing.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.annotation.ComparableFields;
import org.mifosplatform.infrastructure.core.api.JsonCommand;


/**
 * Domain for {@link EventPricing}
 * 
 * @author pavani
 *
 */
@Entity
@Table(name = "b_event_pricing")
@ComparableFields(on={"formatType","optType","clientType","discountId","price"})
public class EventPricing {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	/*@Column(name = "event_id")
	private Integer eventId;*/
	
	@Column(name = "format_type")
	private String formatType;
	
	@Column(name = "opt_type")
	private String optType;
	
	@Column(name = "client_typeid")
	private Long clientType;
	
	@Column(name = "discount_id")
	private Integer discountId;
	
	@Column(name = "price")
	private Double price;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "event_id", insertable = true, updatable = true, nullable = true, unique = true)
	private EventMaster eventId;
	
	@Column(name = "is_deleted")
	private char isDeleted;
	
	public EventPricing() {
		
	}
	
	public static EventPricing fromJson(final JsonCommand command, EventMaster eventMaster) {
		Integer eventId = command.integerValueOfParameterNamed("eventId");
		String formatType = command.stringValueOfParameterNamed("formatType");
		String optType = command.stringValueOfParameterNamed("optType");
		Long clientType = command.longValueOfParameterNamed("clientType");
		Integer discountId = command.integerValueOfParameterNamed("discountId");
		String priceString  = command.stringValueOfParameterNamed("price");
		Double price = Double.parseDouble(priceString);
		return new EventPricing(eventMaster, formatType, optType, clientType, discountId, price);
	}
		
	public EventPricing(EventMaster eventId, String formatType, String optType, Long clientType,
						Integer discountId, Double price) {
		this.eventId = eventId;
		this.formatType = formatType;
		this.optType = optType;
		this.clientType = clientType;
		this.discountId = discountId;
		this.price = price;
		this.isDeleted ='n';
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
	public EventMaster getEventId() {
		return eventId;
	}

	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(EventMaster eventId) {
		this.eventId = eventId;
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
	 * @return the optType
	 */
	public String getOptType() {
		return optType;
	}

	/**
	 * @param optType the optType to set
	 */
	public void setOptType(String optType) {
		this.optType = optType;
	}

	/**
	 * @return the clientType
	 */
	public Long getClientType() {
		return clientType;
	}

	/**
	 * @param clientType the clientType to set
	 */
	public void setClientType(Long clientType) {
		this.clientType = clientType;
	}

	/**
	 * @return the discountId
	 */
	public Integer getDiscountId() {
		return discountId;
	}

	/**
	 * @param discountId the discountId to set
	 */
	public void setDiscountId(Integer discountId) {
		this.discountId = discountId;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @return the isDeleted
	 */
	public char getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(char isDeleted) {
		this.isDeleted = isDeleted;
	}
}
