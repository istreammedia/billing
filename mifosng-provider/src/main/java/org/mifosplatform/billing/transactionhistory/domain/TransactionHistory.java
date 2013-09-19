package org.mifosplatform.billing.transactionhistory.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;


@Entity
@Table(name="b_transaction_history")
public class TransactionHistory extends AbstractAuditableCustom<AppUser, Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="client_id",nullable=false, length=20)
	private Long clientId;
	
	@Column(name="transaction_type", nullable=false, length=100)
	private String transactionType;
	
	@Column(name="transaction_date", nullable=false)
	private Date transactionDate;
	
	@Column(name="history", nullable=false, length=250)
	private String history;
	
	
	public TransactionHistory() {
		
	}
	
	public TransactionHistory(Long clientId, String transactionType, Date transactionDate, String history){
		this.clientId = clientId;
		this.transactionType = transactionType;
		this.transactionDate = transactionDate;
		this.history = history;
	}
	
	public void setClientId(Long clientId){
		this.clientId = clientId;
	}
	
	public Long getClientId(){
		return this.clientId;
	}
	
	public void setTransactionType(String transactionType){
		this.transactionType = transactionType;
	}
	
	public String getTransactionType(){
		return this.transactionType;
	}
	
	public void setTransactionDate(Date transactionDate){
		this.transactionDate = transactionDate;
	}
	
	public Date getTransactionDate(){
		return this.transactionDate;
	}
	
	public void setHistory(String history){
		this.history = history;
	}
	
	public String getHistory(){
		return this.history;
	}

}
