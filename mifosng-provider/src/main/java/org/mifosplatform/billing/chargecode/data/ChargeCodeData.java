package org.mifosplatform.billing.chargecode.data;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.priceregion.data.PriceRegionData;
import org.mifosplatform.billing.taxmapping.data.TaxMapData;


public class ChargeCodeData {	
	
	private Long id;
	
	private String chargeCode;
	
	private String chargeDescription;

	private String chargeType;
	
	private Integer chargeDuration;
	
	private String durationType;

	private Integer taxInclusive;
	
	private String billFrequencyCode;
	
	private List<ChargeCodeData> chargeCodeData;
	private List<ChargeTypeData> chargeTypeData;
	private List<DurationTypeData> durationTypeData;
	private List<BillFrequencyCodeData> billFrequencyCodeData;
	
	
	private Collection<ChargeCodeData> chargeCodeForTax;
	private List<String> chargeDescriptionForTax;
	private List<TaxMapData> taxMapData;

	private List<PriceRegionData> priceRegionData;
	
	
	public ChargeCodeData(final List<TaxMapData> tmd){
		this.taxMapData = tmd;
		
	}
	
	
	public ChargeCodeData() {}
	
	public ChargeCodeData(List<ChargeCodeData> chargeCodeData, List<ChargeTypeData> chargeType, List<DurationTypeData> durationType, List<BillFrequencyCodeData> billFrequencyCodeData){
		this.chargeCodeData = chargeCodeData;
		this.chargeTypeData = chargeType;
		this.durationTypeData = durationType;
		this.billFrequencyCodeData = billFrequencyCodeData;
		
	}
	
	public ChargeCodeData(String chargeCode,String chargeDescription){
		this.chargeCode = chargeCode;
		this.chargeDescription = chargeDescription;
	}
	
	public ChargeCodeData(Long id,String chargeCode, String chargeDescription,String chargeType,Integer chargeDuration,String durationType,Integer taxInclusive,String billFrequencyCode) {
		this.id = id;
		this.chargeCode = chargeCode;
		this.chargeDescription = chargeDescription;
		this.chargeType = chargeType;
		this.chargeDuration = chargeDuration;
		this.durationType = durationType;
		this.taxInclusive = taxInclusive;
		this.billFrequencyCode = billFrequencyCode;
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
	 * @return the chargeDescription
	 */
	public String getChargeDescription() {
		return chargeDescription;
	}

	/**
	 * @param chargeDescription the chargeDescription to set
	 */
	public void setChargeDescription(String chargeDescription) {
		this.chargeDescription = chargeDescription;
	}

	/**
	 * @return the chargeType
	 */
	public String getChargeType() {
		return chargeType;
	}

	/**
	 * @param chargeType the chargeType to set
	 */
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * @return the chargeDuration
	 */
	public Integer getchargeDuration() {
		return chargeDuration;
	}

	/**
	 * @param chargeDuration the chargeDuration to set
	 */
	public void setchargeDuration(Integer chargeDuration) {
		this.chargeDuration = chargeDuration;
	}

	/**
	 * @return the durationType
	 */
	public String getDurationType() {
		return durationType;
	}

	/**
	 * @param durationType the durationType to set
	 */
	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	/**
	 * @return the taxInclusive
	 */
	public Integer getTaxInclusive() {
		return taxInclusive;
	}

	/**
	 * @param taxInclusive the taxInclusive to set
	 */
	public void setTaxInclusive(Integer taxInclusive) {
		this.taxInclusive = taxInclusive;
	}

	/**
	 * @return the billFrequencyCode
	 */
	public String getBillFrequencyCode() {
		return billFrequencyCode;
	}

	/**
	 * @param billFrequencyCode the billFrequencyCode to set
	 */
	public void setBillFrequencyCode(String billFrequencyCode) {
		this.billFrequencyCode = billFrequencyCode;
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
	 * @return the chargeDuration
	 */
	public Integer getChargeDuration() {
		return chargeDuration;
	}


	/**
	 * @param chargeDuration the chargeDuration to set
	 */
	public void setChargeDuration(Integer chargeDuration) {
		this.chargeDuration = chargeDuration;
	}


	/**
	 * @return the chargeCodeData
	 */
	public List<ChargeCodeData> getChargeCodeData() {
		return chargeCodeData;
	}


	/**
	 * @param chargeCodeData the chargeCodeData to set
	 */
	public void setChargeCodeData(List<ChargeCodeData> chargeCodeData) {
		this.chargeCodeData = chargeCodeData;
	}


	/**
	 * @return the chargeTypeData
	 */
	public List<ChargeTypeData> getChargeTypeData() {
		return chargeTypeData;
	}


	/**
	 * @param chargeTypeData the chargeTypeData to set
	 */
	public void setChargeTypeData(List<ChargeTypeData> chargeTypeData) {
		this.chargeTypeData = chargeTypeData;
	}


	/**
	 * @return the durationTypeData
	 */
	public List<DurationTypeData> getDurationTypeData() {
		return durationTypeData;
	}


	/**
	 * @param durationTypeData the durationTypeData to set
	 */
	public void setDurationTypeData(List<DurationTypeData> durationTypeData) {
		this.durationTypeData = durationTypeData;
	}


	/**
	 * @return the billFrequencyData
	 */
	public List<BillFrequencyCodeData> getBillFrequencyCodeData() {
		return billFrequencyCodeData;
	}


	/**
	 * @param billFrequencyData the billFrequencyData to set
	 */
	public void setBillFrequencyCodeData(List<BillFrequencyCodeData> billFrequencyCodeData) {
		this.billFrequencyCodeData = billFrequencyCodeData;
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
	 * @return the chargeDescriptionForTax
	 */
	public List<String> getChargeDescriptionForTax() {
		return chargeDescriptionForTax;
	}


	/**
	 * @param chargeDescriptionForTax the chargeDescriptionForTax to set
	 */
	public void setChargeDescriptionForTax(List<String> chargeDescriptionForTax) {
		this.chargeDescriptionForTax = chargeDescriptionForTax;
	}


	/**
	 * @return the taxMapData
	 */
	public List<TaxMapData> getTaxMapData() {
		return taxMapData;
	}


	/**
	 * @param taxMapData the taxMapData to set
	 */
	public void setTaxMapData(List<TaxMapData> taxMapData) {
		this.taxMapData = taxMapData;
	}


	public void setRegionalTaxData(List<PriceRegionData> priceRegionData) {
             this.priceRegionData=priceRegionData;       
	
	}
	
	

}
