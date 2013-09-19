package org.mifosplatform.billing.billingorder.domain;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.mifosplatform.billing.order.domain.Order;

@Entity
@Table(name = "b_charge_tax")
public class InvoiceTax {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "charge_id", insertable = true, updatable = true, nullable = true, unique = true)
	private BillingOrder charge;

	@ManyToOne
	@JoinColumn(name = "invoice_id", insertable = true, updatable = true, nullable = true, unique = true)
	private Invoice invoice;

	@Column(name = "tax_code")
	private String taxCode;

	@Column(name = "tax_value")
	private BigDecimal taxValue;

	@Column(name = "tax_percentage")
	private BigDecimal taxPercentage;

	@Column(name = "tax_amount")
	private BigDecimal taxAmount;
	
	@Column(name = "bill_id")
	private Long billId;
	


	private InvoiceTax() {

	}

	public InvoiceTax(final Invoice invoice, final BillingOrder charge,
			final String taxCode, final BigDecimal taxValue,
			final BigDecimal taxPercentage, final BigDecimal taxAmount) {

		this.charge = charge;
		this.invoice = invoice;
		this.taxCode = taxCode;
		this.taxValue = taxValue;
		this.taxPercentage = taxPercentage;
		this.taxAmount = taxAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public BillingOrder getCharge() {
		return charge;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public Long getBillId() {
		return billId;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public BigDecimal getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(BigDecimal taxValue) {
		this.taxValue = taxValue;
	}

	public BigDecimal getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(BigDecimal taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public void updateBillId(Long billId) {
		this.billId=billId;
		
	}
	
	public void update(BillingOrder charge) {
		this.charge = charge;

	}

}
