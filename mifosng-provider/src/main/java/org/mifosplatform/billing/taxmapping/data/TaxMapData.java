package org.mifosplatform.billing.taxmapping.data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.chargecode.data.ChargeCodeData;
import org.mifosplatform.billing.priceregion.data.PriceRegionData;

public class TaxMapData {
	
	private Long id;
	private String chargeCode;
	
	
	private String taxCode;
	
	
	private LocalDate startDate;
	
	
	private String type;
	
	
	private BigDecimal rate;

	private Collection<ChargeCodeData> chargeCodeForTax;
	private Collection<TaxMapData> taxMapData;
	private String TaxRegion;
	private Long TaxRegionId;
	private List<PriceRegionData> priceRegionData;
	
	
	public TaxMapData(){}
	
	public TaxMapData(String chargeCode,String taxCode,LocalDate startDate,String type,BigDecimal rate){
		this.chargeCode = chargeCode;
		this.taxCode = taxCode;
		this.startDate = startDate;
		this.type = type;
		this.rate = rate;
	}
	
	public TaxMapData(Long id, String chargeCode,String taxCode,LocalDate startDate,String type,BigDecimal rate,String region,Long TaxRegionId){
		this.id=id;
		this.chargeCode = chargeCode;
		this.taxCode = taxCode;
		this.startDate = startDate;
		this.type = type;
		this.rate = rate;
		this.TaxRegion=region;
		this.TaxRegionId=TaxRegionId;
	}
	
	public TaxMapData(Long id, String type){
		this.type = type;
		this.id = id;
	}

	public TaxMapData(Collection<ChargeCodeData> ccd, Collection<TaxMapData> tmd,Collection<TaxMapData> taxMapData) {
		this.chargeCodeForTax = ccd;
		this.taxMapData = tmd;
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
	 * @return the taxCode
	 */
	public String getTaxCode() {
		return taxCode;
	}

	/**
	 * @param taxCode the taxCode to set
	 */
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the rate
	 */
	public BigDecimal getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(BigDecimal rate) {
		this.rate = rate;
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
	 * @return the chargeCodeForTax
	 */
	public Collection<ChargeCodeData> getChargeCodeForTax() {
		return chargeCodeForTax;
	}

	/**
	 * @param chargeCodeForTax the chargeCodeForTax to set
	 */
	public void setChargeCodeForTax(Collection<ChargeCodeData> chargeCodeForTax) {
		this.chargeCodeForTax = chargeCodeForTax;
	}

	/**
	 * @return the taxMapData
	 */
	public Collection<TaxMapData> getTaxMapData() {
		return taxMapData;
	}

	/**
	 * @param taxMapData the taxMapData to set
	 */
	public void setTaxMapData(Collection<TaxMapData> taxMapData) {
		this.taxMapData = taxMapData;
	}

	public void setRegionalTaxData(List<PriceRegionData> priceRegionData) {
	  
		this.priceRegionData=priceRegionData;
		
	}
	
}
