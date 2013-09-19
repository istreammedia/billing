package org.mifosplatform.billing.chargecode.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Table(name = "b_charge_codes", uniqueConstraints = @UniqueConstraint(name = "chargecode", columnNames = { "charge_code" }))
public class ChargeCode extends AbstractPersistable<Long>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Column(name = "charge_code")
	private String chargeCode;
	
	@Column(name = "charge_description")
	private String chargeDescription;
	
	@Column(name = "charge_type")
	private String chargeType;
	
	@Column(name = "charge_duration")
	private Integer chargeDuration;
	
	@Column(name = "duration_type")
	private String durationType;
	
	@Column(name = "tax_inclusive")
	private Integer taxInclusive;
	
	@Column(name = "billfrequency_code")
	private String billFrequencyCode;
	
	public ChargeCode() {}
	
	public ChargeCode(String chargeCode, String chargeDescription,String chargeType,Integer chargeDuration,String durationType,Integer taxInclusive,String billFrequencyCode) {
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
	
	
	public ChargeCode(String billFrequencyCode,String chargeCode, String chargeDescription,Integer chargeDuration,String chargeType,String durationType,Integer taxInclusive){
		this.billFrequencyCode = billFrequencyCode;
		this.chargeCode = chargeCode;
		this.chargeDescription = chargeDescription;
		this.chargeDuration = chargeDuration;
		this.chargeType = chargeType;
		this.durationType = durationType;
		this.taxInclusive = taxInclusive;
		
	}
	
	public static ChargeCode fromJson(JsonCommand command){
		final String billFrequencyCode = command.stringValueOfParameterNamed("billFrequencyCode");
		final String chargeCode = command.stringValueOfParameterNamed("chargeCode");
		final String chargeDescription = command.stringValueOfParameterNamed("chargeDescription");
		final Integer chargeDuration = command.integerValueOfParameterNamed("chargeDuration");
		final String chargeType = command.stringValueOfParameterNamed("chargeType");
		final String durationType = command.stringValueOfParameterNamed("durationType");
		final boolean taxInclusive = command.booleanPrimitiveValueOfParameterNamed("taxInclusive");
		
		Integer tax = null;
		
		if(taxInclusive){
			tax = 1;
		}else{ 
			tax = 0;
		}
		return new ChargeCode(billFrequencyCode,chargeCode,chargeDescription,chargeDuration,chargeType,durationType,tax);
	}
	
	public Map<String,Object> update(JsonCommand command){
		
		final Map<String,Object> actualChanges = new LinkedHashMap<String,Object>(1);
		if(command.isChangeInStringParameterNamed("chargeCode",this.chargeCode)){
			final String newValue = command.stringValueOfParameterNamed("chargeCode");
			actualChanges.put("chargeCode",newValue);
			this.chargeCode = StringUtils.defaultIfEmpty(newValue, null);
		}
		if(command.isChangeInStringParameterNamed("chargeDescription", this.chargeDescription)){
			final String newValue = command.stringValueOfParameterNamed("chargeDescription");
			actualChanges.put("chargeDescription", newValue);
			this.chargeDescription = StringUtils.defaultIfEmpty(newValue, null);
		}
		if(command.isChangeInStringParameterNamed("chargeType", this.chargeType)){
			final String newValue = command.stringValueOfParameterNamed("chargeType");
			actualChanges.put("chargeType", newValue);
			this.chargeType = StringUtils.defaultIfEmpty(newValue, null);
		}
		if(command.isChangeInIntegerParameterNamed("chargeDuration", this.chargeDuration)){
			final Integer newValue = command.integerValueOfParameterNamed("chargeDuration");
			actualChanges.put("chargeDuration", newValue);
			this.chargeDuration = newValue;
		}
		if(command.isChangeInStringParameterNamed("durationType", this.durationType)){
			final String newValue = command.stringValueOfParameterNamed("durationType");
			actualChanges.put("durationType",newValue);
			this.durationType = StringUtils.defaultIfEmpty(newValue, null);
		}
		if(command.isChangeInBooleanParameterNamed("taxInclusive", this.taxInclusive==1?true:false)){
			final boolean taxInclusive = command.booleanPrimitiveValueOfParameterNamed("taxInclusive");
			
			Integer newValue = null;
			
			if(taxInclusive){
				newValue = 1;
			}else{ 
				newValue = 0;
			}
			actualChanges.put("taxInclusive", newValue);
			this.taxInclusive = newValue;
		}
		if(command.isChangeInStringParameterNamed("billFrequencyCode", this.billFrequencyCode)){
			final String newValue = command.stringValueOfParameterNamed("billFrequencyCode");
			actualChanges.put("billFrequencyCode", newValue);
			this.billFrequencyCode = StringUtils.defaultIfEmpty(newValue, null);
			
		}
		
		return actualChanges;
	}
	
	
	

}
