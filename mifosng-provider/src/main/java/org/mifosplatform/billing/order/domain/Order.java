package org.mifosplatform.billing.order.domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;

@Entity
@Table(name = "b_orders")
public class Order extends AbstractAuditableCustom<AppUser, Long> {

	/*@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;*/

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "plan_id")
	private Long planId;

	@Column(name = "order_status")
	private Long status;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "billing_frequency")
	private String billingFrequency;

	@Column(name = "next_billable_day")
	private Date nextBillableDay;

	@Column(name = "start_date")
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "contract_period")
	private Long contarctPeriod;

	@Column(name = "is_deleted")
	private char isDeleted;

	@Column(name = "billing_align")
	private char billingAlign;
	
	@Column(name ="disconnect_reason")
	private String disconnectReason;
	
	
	@Column(name ="user_action")
	private String userAction;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orders", orphanRemoval = true)
	private List<OrderLine> services = new ArrayList<OrderLine>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orders", orphanRemoval = true)
	private List<OrderPrice> price = new ArrayList<OrderPrice>();
	
	
	//for OrderDiscount
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orderId", orphanRemoval = true)
	private List<OrderDiscount> orderDiscount = new ArrayList<OrderDiscount>();

	 public Order() {
		// TODO Auto-generated constructor stub
			
	}
 
	
	public Order(Long client_id, Long plan_id, Long status, Long duration_type,String billingFreq,
			LocalDate startDate, LocalDate endDate, Long contract,
			List<OrderLine> serviceDetails, List<OrderPrice> orderprice,char billalign,String userAction) {
		this.clientId = client_id;
		this.planId = plan_id;
		this.status = status;
		this.transactionType = "Add Order";
		this.billingFrequency =billingFreq;
		this.startDate = startDate.toDate();
		if (endDate != null)
			this.endDate = endDate.toDate();
		this.services = serviceDetails;
		this.price = orderprice;
		this.contarctPeriod = contract;
		this.billingAlign=billalign;
		this.isDeleted='n';
		this.userAction=userAction;
	}

public Order(Long clientId, Long planId, Long contractPeriod, String paytermCode, char billAlign,LocalDate startdate) {
	    this.clientId=clientId;
	    this.planId=planId;
	    this.contarctPeriod=contractPeriod;
	    this.billingFrequency=paytermCode;
	    this.billingAlign=billAlign;
	    this.startDate=startdate.toDate();
	}
	public Long getClientId() {
		return clientId;
	}

	public Long getPlanId() {
		return planId;
	}

	public Long getStatus() {
		return status;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}


	public Date getNextBillableDay() {
		return nextBillableDay;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public List<OrderLine> getServices() {
		return services;
	}

	public List<OrderPrice> getPrice() {
		return price;
	}

	public void addServiceDeatils(OrderLine orderDetail) {
		orderDetail.update(this);
		this.services.add(orderDetail);

	}

	public void addOrderDeatils(OrderPrice price) {
		price.update(this);
		this.price.add(price);

	}

	public Long getContarctPeriod() {
		return contarctPeriod;
	}

	public void delete() {
		this.isDeleted = 'y';
        this.endDate=new LocalDate().toDate();
	}

	public void update(JsonCommand command, Long orderStatus) {

		if (this.status != 3) {
			this.endDate = new LocalDate().toDate();
			this.disconnectReason=command.stringValueOfParameterNamed("disconnectReason");

			this.status =orderStatus;
		}

	
	}

	public static Order fromJson(Long clientId, JsonCommand command) {
		 final Long planId = command.longValueOfParameterNamed("planCode");
		 final LocalDate startDate=command.localDateValueOfParameterNamed("start_date");
		    final Long contractPeriod = command.longValueOfParameterNamed("contractPeriod");
		    final String paytermCode = command.stringValueOfParameterNamed("paytermCode");
		    final boolean billAlign=command.booleanPrimitiveValueOfParameterNamed("billAlign");
		    char align=billAlign?'y':'n';
		    return new Order(clientId,planId,contractPeriod,paytermCode,align,startDate);
	}

	public char getbillAlign() {
		// TODO Auto-generated method stub
		return billingAlign;
	}


	public void setNextBillableDay(Date nextBillableDate) {
		this.nextBillableDay=nextBillableDate;
		
	}


	public void addOrderDisount(OrderDiscount orderDiscount) {

		this.orderDiscount.add(orderDiscount);
	}


	public void setEndDate(LocalDate renewalDate) {
		this.endDate=null;
		if(renewalDate!=null){
		this.endDate=renewalDate.toDate();
		}
	}


	public void setStartDate(LocalDate startDate) {

		this.startDate=startDate.toDate();
	}


	public void setStatus(Long statusId) {
		this.status=statusId;
		
	}

}
