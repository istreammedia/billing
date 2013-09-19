package org.mifosplatform.billing.onetimesale.command;

import java.math.BigDecimal;
import java.util.Set;

import org.joda.time.LocalDate;

public class OneTimeSaleCommand {
	private final Long itemId;
	private final String units;
	private final String chargeCode;
	private final String quantity;
	private final BigDecimal unitPrice;
	private final BigDecimal totalPrice;
	private final Set<String> modifiedParameters;
	private final LocalDate saleDate; 

	public OneTimeSaleCommand(Set<String> modifiedParameters, Long itemId,
			String units, String chargeCode, BigDecimal unitPrice,
			String quantity, BigDecimal totalPrice, LocalDate saleDate) {
		this.itemId = itemId;
		this.modifiedParameters = modifiedParameters;
		this.units = units;
		this.unitPrice = unitPrice;
		this.chargeCode = chargeCode;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.saleDate=saleDate;

	}

	public Long getItemId() {
		return itemId;
	}

	public String getUnits() {
		return units;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public String getQuantity() {
		return quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	
	

	public LocalDate getSaleDate() {
		return saleDate;
	}

	public Set<String> getModifiedParameters() {
		return modifiedParameters;
	}

}
