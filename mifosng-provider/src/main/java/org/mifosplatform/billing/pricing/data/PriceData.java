package org.mifosplatform.billing.pricing.data;

import java.math.BigDecimal;

public class PriceData {

	private Long id;
	private String chargeCode;
	private String serviceCode;
	private String charging_variant;
	private BigDecimal price;
	private String chagreType;
	private String chargeDuration;
	private String durationType;
	private Long serviceId;
	private Long discountId;
	private boolean taxInclusive;
	private Long clientStateId;
	private Long regionStateId;
	private Long priceRegionCountry;
	private Long clientCountry;
	public PriceData(final Long id,final String serviceCode,final String chargeCode,
			final String charging_variant,final BigDecimal price,final String chrgeType,
			final String chargeDuration,final String durationType,final Long serviceId, Long discountId, boolean taxinclusive,
			Long stateId, Long countryId, Long regionState, Long regionCountryId)
	{

		this.id=id;
		this.chargeCode=chargeCode;
		this.serviceCode=serviceCode;
		this.charging_variant=charging_variant;
		this.price=price;
		this.chagreType=chrgeType;
		this.chargeDuration=chargeDuration;
		this.durationType=durationType;
		this.serviceId=serviceId;
		this.discountId=discountId;
		this.taxInclusive=taxinclusive;
		this.clientStateId=stateId;
	    this.clientCountry=countryId;
	    this.regionStateId=regionState;
	    this.priceRegionCountry=regionCountryId;
	    
	}
	public Long getId() {
		return id;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public String getCharging_variant() {
		return charging_variant;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public String getChagreType() {
		return chagreType;
	}
	public String getChargeDuration() {
		return chargeDuration;
	}
	public String getDurationType() {
		return durationType;
	}
	public Long getServiceId() {
		return serviceId;
	}
	
	
	public Long getClientStateId() {
		return clientStateId;
	}
	public Long getRegionStateId() {
		return regionStateId;
	}
	
	
	public Long getPriceRegionCountry() {
		return priceRegionCountry;
	}
	public Long getClientCountry() {
		return clientCountry;
	}
	public Long getDiscountId() {
		return discountId;
	}
	/**
	 * @return the taxInclusive
	 */
	public boolean isTaxInclusive() {
		return taxInclusive;
	}

	
}
