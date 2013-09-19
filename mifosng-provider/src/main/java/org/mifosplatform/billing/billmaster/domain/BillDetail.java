package org.mifosplatform.billing.billmaster.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "b_bill_details")
public class BillDetail {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@ManyToOne
    @JoinColumn(name="bill_id")
    private BillMaster billMaster;

	@Column(name ="transaction_id")
	private  Long transactionId;

	@Column(name="Transaction_date")
	private Date transactionDate;

	@Column(name = "Transaction_type")
	private String transactionType;

	@Column(name = "Amount")
	private BigDecimal amount;

	protected BillDetail() {

	}

	public BillDetail(final BillMaster billId,final Long transactionId ,final Date transactionDate, final String transactionType,
			final BigDecimal amount) {

		this.billMaster = billId;
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.transactionType = transactionType;
		this.amount = amount;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BillMaster getBillId() {
		return billMaster;
	}

	public void setBillId(BillMaster billId) {
		this.billMaster = billId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public void updateBillMaster(BillMaster billMaster) {
           this.billMaster=billMaster;
		
		
	}


}
