/**
 * 
 */
package org.mifosplatform.billing.eventpricing.data;

import java.math.BigDecimal;
import java.util.List;

import org.mifosplatform.billing.eventpricing.domain.EventPricing;
import org.mifosplatform.billing.mediadetails.data.MediaAssetLocationDetails;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

/**
 * POJO for {@link EventPricing}
 * 
 * @author pavani
 *
 */
public class EventPricingData {

	private Long id;
	private Long eventId;
	private String formatType;
	private String optType;
	private Long clientTypeId;
	private String discount;
	private BigDecimal price;
	private String customerCategory;
	private String eventName;
	private List<EnumOptionData> optTypes;
	private List<MediaAssetLocationDetails> format;
	private List<DiscountMasterData> discountdata;
	private List<ClientTypeData> clientTypes;
	private String clientType;
	
	
	
	
	public EventPricingData(Long id, String eventName, String formatType, String optType,Long clientTypeId,
			String discount, BigDecimal price, Long eventId, String clientType) {
		this.id =id;
		this.eventName = eventName;
		this.optType =optType;
		this.formatType = formatType;
		this.clientTypeId = clientTypeId;
		this.discount = discount;
		this.price = price;
		this.eventId=eventId;
		this.clientType=clientType;
	}
	
	public EventPricingData(List<EnumOptionData> optTypes, List<MediaAssetLocationDetails> format,
							List<DiscountMasterData> discountdata, List<ClientTypeData> clientTypes , Long eventId) {
		this.optTypes = optTypes;
		this.format = format;
		this.discountdata = discountdata;
		this.eventId = eventId;
		this.clientTypes = clientTypes;
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
	public Long getEventId() {
		return eventId;
	}
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(Long eventId) {
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
	public void setOptType(String OptType) {
		optType = OptType;
	}
	
	/**
	 * @return the optTypes
	 */
	public List<EnumOptionData> getOptTypes() {
		return optTypes;
	}

	/**
	 * @param optTypes the optTypes to set
	 */
	public void setOptTypes(List<EnumOptionData> optTypes) {
		this.optTypes = optTypes;
	}

	/**
	 * @return the format
	 */
	public List<MediaAssetLocationDetails> getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(List<MediaAssetLocationDetails> format) {
		this.format = format;
	}

	/**
	 * @return the discountdata
	 */
	public List<DiscountMasterData> getDiscountdata() {
		return discountdata;
	}

	/**
	 * @param discountdata the discountdata to set
	 */
	public void setDiscountdata(List<DiscountMasterData> discountdata) {
		this.discountdata = discountdata;
	}

	/**
	 * @return the clientTypes
	 */
	public List<ClientTypeData> getClientTypes() {
		return clientTypes;
	}

	/**
	 * @param clientTypes the clientTypes to set
	 */
	public void setClientTypes(List<ClientTypeData> clientTypes) {
		this.clientTypes = clientTypes;
	}

	/**
	 * @return the clientType
	 */
	public Long getClientType() {
		return clientTypeId;
	}

	/**
	 * @param clientType the clientType to set
	 */
	public void setClientType(Long clientType) {
		this.clientTypeId = clientType;
	}

	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the customerCategory
	 */
	public String getCustomerCategory() {
		return customerCategory;
	}

	/**
	 * @param customerCategory the customerCategory to set
	 */
	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
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
}
