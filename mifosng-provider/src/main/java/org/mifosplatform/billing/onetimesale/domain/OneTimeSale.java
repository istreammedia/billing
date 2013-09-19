package org.mifosplatform.billing.onetimesale.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.xmlbeans.impl.tool.XSTCTester.Harness;
import org.joda.time.LocalDate;
import org.mifosplatform.billing.item.domain.ItemMaster;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;

@Entity
@Table(name = "b_onetime_sale")
public class OneTimeSale extends AbstractAuditableCustom<AppUser, Long> {



	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "units", length = 65536)
	private String units;

	@Column(name = "charge_code")
	private String chargeCode;

	@Column(name = "unit_price")
	private BigDecimal unitPrice;
	
	@Column(name = "quantity")
	private String quantity;

	@Column(name = "total_price")
	private BigDecimal totalPrice;

	@Column(name = "sale_date")
	private Date saleDate;

	@Column(name = "item_id")
	private Long itemId;
	
	@Column(name = "bill_id")
	private Long billId;

	@Column(name = "is_invoiced", nullable = false)
	private char deleted = 'n';
	
	@Column(name="hardware_allocated",nullable=false)
	private String hardwareAllocated = "UNALLOCATED";

	@Column(name = "discount_id")
	private Long discountId;
	
	public OneTimeSale(){}
	
	public OneTimeSale(Long clientId, Long itemId,String units,String quantity,
			 String chargeCode, BigDecimal unitPrice,
			BigDecimal totalPrice, LocalDate saleDate, Long discountId) {

	this.clientId=clientId;
	this.itemId=itemId;
	this.units=units;
	this.chargeCode=chargeCode;
	this.unitPrice=unitPrice;
	this.totalPrice=totalPrice;
	this.quantity=quantity;
	this.saleDate=saleDate.toDate();
	this.discountId=discountId;
		}

	public Long getClientId() {
		return clientId;
	}

	public String getUnits() {
		return units;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public String getQuantity() {
		return quantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public Long getItemId() {
		return itemId;
	}

	public char getDeleted() {
		return deleted;
	}

	public void setDeleted(char deleted) {
		this.deleted = deleted;
	}
	
	public void updateBillId(Long billId) {
        this.billId=billId;
       
    }
	public static OneTimeSale fromJson(Long clientId, JsonCommand command, ItemMaster item) {
		    final Long itemId=command.longValueOfParameterNamed("itemId");
		    final String units = item.getUnits();
		    final String chargeCode = command.stringValueOfParameterNamed("chargeCode");
		    final String quantity = command.stringValueOfParameterNamed("quantity");
		    final BigDecimal unitPrice=command.bigDecimalValueOfParameterNamed("unitPrice");
		    final BigDecimal totalPrice=command.bigDecimalValueOfParameterNamed("totalPrice");
		    final LocalDate saleDate=command.localDateValueOfParameterNamed("saleDate");
		    final Long discountId=command.longValueOfParameterNamed("discountId");
          return new OneTimeSale(clientId, itemId, units, quantity, chargeCode, unitPrice, totalPrice, saleDate,discountId);
		 
	}
	public String getHardwareAllocated() {
		return hardwareAllocated;
	}

	public void setHardwareAllocated(String hardwareAllocated) {
		this.hardwareAllocated = hardwareAllocated;
	}


}
