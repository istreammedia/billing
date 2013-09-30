package org.mifosplatform.billing.taxmapping.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;
/*import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;*/


@Entity
@Table(name = "b_tax_mapping_rate")//, uniqueConstraints = @UniqueConstraint(name = "taxcode", columnNames = { "tax_code" }))
public class TaxMap extends AbstractPersistable<Long>{
	
	@Column(name="charge_code", length=10, nullable=false)
	private String chargeCode;
	
	@Column(name="tax_code", length=10, nullable=false)
	private String taxCode;
	
	@Column(name="start_date", nullable=false)
	private Date startDate;
	
	@Column(name="type", length=15, nullable=false)
	private String type;
	
	@Column(name="rate",nullable=false)
	private BigDecimal rate;
	
	@Column(name="tax_region_id",nullable=false)
	private Long taxRegion;

	
	public TaxMap(){}
	
	public TaxMap(final String chargeCode, final String taxCode, final Date startDate,final String type,final BigDecimal rate, Long taxregion){
		this.chargeCode = chargeCode;
		this.taxCode = taxCode;
		this.startDate = startDate;
		this.type = type;
		this.rate = rate;
		this.taxRegion=taxregion;
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
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
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
	
	public static TaxMap fromJson(JsonCommand command){
		String chargeCode = command.stringValueOfParameterNamed("chargeCode");
		String taxCode = command.stringValueOfParameterNamed("taxCode");
		LocalDate startDate = command.localDateValueOfParameterNamed("startDate");
		String type = command.stringValueOfParameterNamed("type");
		BigDecimal rate = command.bigDecimalValueOfParameterNamed("rate");
		Long taxregion= command.longValueOfParameterNamed("taxRegion");
		
		return new TaxMap(chargeCode,taxCode,startDate.toDate(),type,rate,taxregion);
	}
	
	public Map<String,Object> update(JsonCommand command){
		final Map<String,Object> actualChanges = new LinkedHashMap<String, Object>(1);
		if(command.isChangeInStringParameterNamed("chargeCode", this.chargeCode)){
			
			final String newValue = command.stringValueOfParameterNamed("chargeCode");
			actualChanges.put("chargeCode", newValue);
			this.chargeCode = StringUtils.defaultIfEmpty(newValue, null);
			
		}if(command.isChangeInStringParameterNamed("taxCode", this.taxCode)){
			
			final String newValue = command.stringValueOfParameterNamed("taxCode");
			actualChanges.put("taxCode", newValue);
			this.taxCode = StringUtils.defaultIfEmpty(newValue, null);			
		
		}if(command.isChangeInDateParameterNamed("startDate", this.startDate)){
			
			final LocalDate newValue = command.localDateValueOfParameterNamed("startDate");
			actualChanges.put("startDate", newValue);
			this.startDate = newValue.toDate();
			
		}if(command.isChangeInStringParameterNamed("type", this.type)){
			
			final String newValue = command.stringValueOfParameterNamed("type");
			actualChanges.put("type", newValue);
			this.type = StringUtils.defaultIfEmpty(newValue, null);
		
		}if(command.isChangeInBigDecimalParameterNamed("rate", this.rate)){
			
			final BigDecimal newValue = command.bigDecimalValueOfParameterNamed("rate");
			actualChanges.put("rate", newValue);
			this.rate = newValue;
		}if(command.isChangeInLongParameterNamed("taxRegion", this.taxRegion)){
			
			final Long newValue = command.longValueOfParameterNamed("taxRegion");
			actualChanges.put("rate", newValue);
			this.taxRegion= newValue;
		}
		
		
		return actualChanges;
	}
	
	
}
