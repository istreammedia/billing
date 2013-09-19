package org.mifosplatform.billing.payments.data;

import java.math.BigDecimal;
import java.util.Collection;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.paymode.data.McodeData;

public class PaymentData {
	
    private Collection<McodeData> data;
	private LocalDate paymentDate;
	private String clientName;
	private BigDecimal amountPaid;
	private String payMode;
	private Boolean isDeleted;
	private Long billNumber;
	private String receiptNo;
	
	public PaymentData(Collection<McodeData> data){
		this.data= data;
		this.paymentDate=new LocalDate();
		
	}
	
	
	public PaymentData(String clientName, String payMode,LocalDate paymentDate, BigDecimal amountPaid, Boolean isDeleted, Long billNumber, String receiptNumber) {
		  this.clientName = clientName;
		  this.payMode = payMode;
		  this.paymentDate = paymentDate;
		  this.amountPaid = amountPaid;
		  this.isDeleted = isDeleted;
		  this.billNumber = billNumber;
		  this.receiptNo = receiptNumber;
		 }


	public Collection<McodeData> getData() {
		return data;
	}


	public LocalDate getPaymentDate() {
		return paymentDate;
	}


	public String getClientName() {
		return clientName;
	}


	public BigDecimal getAmountPaid() {
		return amountPaid;
	}


	public String getPayMode() {
		return payMode;
	}


	public Boolean getIsDeleted() {
		return isDeleted;
	}


	public Long getBillNumber() {
		return billNumber;
	}


	public String getReceiptNo() {
		return receiptNo;
	}
	
	
}
