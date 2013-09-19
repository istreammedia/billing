package org.mifosplatform.billing.billingorder.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.mifosplatform.billing.order.domain.Order;
import org.mifosplatform.billing.order.domain.OrderDiscount;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_charge")
public class BillingOrder  extends AbstractPersistable<Long>{

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "priceline_id")
	private Long orderlineId;

	@Column(name = "charge_code")
	private String chargeCode;

	@Column(name = "charge_type")
	private String chargeType;

	@Column(name = "discount_code")
	private String discountCode;

	@Column(name = "charge_amount")
	private BigDecimal chargeAmount;

	@Column(name = "discount_amount")
	private BigDecimal discountAmount;

	@Column(name = "netcharge_amount")
	private BigDecimal netChargeAmount;

	@Column(name = "charge_start_date")
	private Date startDate;

	@Column(name = "charge_end_date")
	private Date entDate;

/*	@Column(name = "invoice_id")
	private Long invoiceId;*/
	
	@Column(name="bill_id")
	private Long billId;
	
	@ManyToOne
	@JoinColumn(name = "invoice_id", insertable = true, updatable = true, nullable = true, unique = true)
	private Invoice invoice;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "charge", orphanRemoval = true)
	private List<InvoiceTax> chargeTaxs = new ArrayList<InvoiceTax>();
	
	

	public BillingOrder() {
	}

	public BillingOrder(final Long clientId, final Long orderId,
			final Long orderlineId, final String chargeCode,
			final String chargeType, final String discountCode,
			final BigDecimal chargeAmount, final BigDecimal discountAmount,
			final BigDecimal netChargeAmount, final Date startDate,
			final Date entDate) {

		this.clientId = clientId;
		this.orderId = orderId;
		this.orderlineId = orderlineId;
		this.chargeCode = chargeCode;
		this.chargeType = chargeType;
		this.discountCode = discountCode;
		this.chargeAmount = chargeAmount;
		this.discountAmount = discountAmount;
		this.netChargeAmount = netChargeAmount;
		this.startDate = startDate;
		this.entDate = entDate;
		this.invoice = null;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderlineId() {
		return orderlineId;
	}

	public void setOrderlineId(Long orderlineId) {
		this.orderlineId = orderlineId;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getNetChargeAmount() {
		return netChargeAmount;
	}

	public void setNetChargeAmount(BigDecimal netChargeAmount) {
		this.netChargeAmount = netChargeAmount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEntDate() {
		return entDate;
	}

	public void setEntDate(Date entDate) {
		this.entDate = entDate;
	}

/*	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}*/

	public void updateBillId(Long billId) {
		this.billId=billId;
		
	}
	
	public Invoice getInvoice() {
		return invoice;
	}

	public void update(Invoice invoice) {
		this.invoice = invoice;

	}
	
	public void addChargeTaxes(InvoiceTax chargeTaxs) {
		chargeTaxs.update(this);
		this.chargeTaxs.add(chargeTaxs);

	}

}
