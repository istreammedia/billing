package org.mifosplatform.billing.item.domain;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.billing.item.exception.ItemNotFoundException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_item_master")
public class ItemMaster extends AbstractPersistable<Long>{


	@Column(name = "item_code")
	private String itemCode;

	@Column(name = "unit_price")
	private BigDecimal unitPrice;
	
	@Column(name = "item_description")
	private String itemDescription;

	@Column(name = "item_class")
	private String itemClass;
	
	@Column(name = "units")
	private String units;
	
	@Column(name = "charge_code")
	private String chargeCode;

	
	@Column(name = "warranty")
	private Long warranty;

	@Column(name = "is_deleted", nullable = false)
	private char deleted = 'n';
	
	public ItemMaster(){}
	
	public ItemMaster(String itemCode, String itemDescription,
			String itemClass, BigDecimal unitPrice, String units,
			Long warranty, String chargeCode) {
             this.itemCode=itemCode;
             this.itemDescription=itemDescription;
             this.itemClass=itemClass;
             this.chargeCode=chargeCode;
             this.units=units;
             this.warranty=warranty;
             this.unitPrice=unitPrice;
             
	
	
	}

	public String getItemCode() {
		return itemCode;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public String getItemClass() {
		return itemClass;
	}

	public String getUnits() {
		return units;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	
	public Long getWarranty() {
		return warranty;
	}

	public char getDeleted() {
		return deleted;
	}

	
	public Map<String, Object> update(JsonCommand command){
		if("Y".equals(deleted)){
			throw new ItemNotFoundException(command.entityId().toString());
		}
		
		final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);
		
		final String firstnameParamName = "itemCode";
		final String secondnameParamName = "itemDescription";
		final String thirdnamedParamName = "itemClass";
		final String fourthnamedParamName = "chargeCode";
		final String fifthnamedParamName = "units";
		final String sixthnamedParamName = "warranty";
		final String seventhnamedParamName = "unitPrice";
		
		if(command.isChangeInStringParameterNamed(firstnameParamName, this.itemCode)){
			final String newValue = command.stringValueOfParameterNamed(firstnameParamName);
			actualChanges.put(fifthnamedParamName, newValue);
			this.itemCode = StringUtils.defaultIfEmpty(newValue,null);
		}
		if(command.isChangeInStringParameterNamed(secondnameParamName, this.itemDescription)){
			final String newValue = command.stringValueOfParameterNamed(secondnameParamName);
			actualChanges.put(secondnameParamName, newValue);
			this.itemDescription = StringUtils.defaultIfEmpty(newValue, null);
		}
		if(command.isChangeInStringParameterNamed(thirdnamedParamName,this.itemClass)){
			final String newValue = command.stringValueOfParameterNamed(thirdnamedParamName);
			actualChanges.put(thirdnamedParamName, newValue);
			this.itemClass =StringUtils.defaultIfEmpty(newValue,null);
		}
		if(command.isChangeInStringParameterNamed(fourthnamedParamName,this.chargeCode)){
			final String newValue = command.stringValueOfParameterNamed(fourthnamedParamName);
			actualChanges.put(fourthnamedParamName, newValue);
			this.chargeCode = StringUtils.defaultIfEmpty(newValue,null);
		}
		if(command.isChangeInStringParameterNamed(fifthnamedParamName,this.units)){
			final String newValue = command.stringValueOfParameterNamed(fifthnamedParamName);
			actualChanges.put(fifthnamedParamName, newValue);
			this.units = StringUtils.defaultIfEmpty(newValue,null); 
		}
		
		if(command.isChangeInLongParameterNamed(sixthnamedParamName, this.warranty)){
			final Long newValue = command.longValueOfParameterNamed(sixthnamedParamName);
			actualChanges.put(sixthnamedParamName, newValue);
			this.warranty = newValue;
		}
		
		if(command.isChangeInBigDecimalParameterNamed(seventhnamedParamName, this.unitPrice)){
			final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(seventhnamedParamName);
			actualChanges.put(seventhnamedParamName,newValue);
			this.unitPrice = newValue;
		}
		
		return actualChanges;
	
	}
	
	

	public void delete() {
		this.deleted='Y';
		
	}
	
	
	
	

}
