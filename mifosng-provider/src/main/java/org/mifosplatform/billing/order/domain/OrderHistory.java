package org.mifosplatform.billing.order.domain;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_orders_history")
public class OrderHistory extends AbstractPersistable<Long> {

	
	

	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "transaction_date")
	private Date transactionDate;

	@Column(name = "actual_date")
	private Date actualDate;

	@Column(name = "prepare_id")
	private Long prepareId;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "createdby_id")
	private Long createdbyId;


	 public OrderHistory() {
		// TODO Auto-generated constructor stub
			
	}


	public OrderHistory(Long orderId, LocalDate transactionDate, LocalDate actualDate,
			Long provisioningId, String tranType, Long userId) {
		
		this.orderId=orderId;
		this.transactionDate=transactionDate.toDate();
		this.actualDate=actualDate.toDate();
		this.prepareId=provisioningId;
		this.transactionType=tranType;
		this.createdbyId=userId;
		this.createdDate=new Date();
	}
 
	
	
}
