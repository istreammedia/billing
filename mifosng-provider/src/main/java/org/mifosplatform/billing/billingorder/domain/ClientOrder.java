package org.mifosplatform.billing.billingorder.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "b_orders")
public class ClientOrder  {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "plan_id")
	private Long planId;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "order_status")
	private String orderStatus;

	@Column(name = "billing_frequency")
	private String billingFrequency;

	@Column(name = "next_billable_day")
	private Date nextBillableDay;

	@Column(name = "billing_align")
	private String billingAlign;

	@Column(name = "createdby_id")
	private Long createdById;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "lastmodified_date")
	private Date lastModifiedDate;

	@Column(name = "lastmodifiedby_id")
	private Long lastModifiedById;

	protected ClientOrder() {

	}

	public ClientOrder(final long id, final Long clientId, final Long planId,
			final Date startDate, final Date endDate,
			final String transactionType, final String orderStatus,
			final String billingFrequency, final Date nextBillableDay,
			final String billingAlign, final Long createdById,
			final Date createdDate, final Date lastModifiedDate,
			final Long lastModifiedById) {

		this.id = id;
		this.clientId = clientId;
		this.planId = planId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.transactionType = transactionType;
		this.orderStatus = orderStatus;
		this.billingFrequency = billingFrequency;
		this.billingAlign = billingAlign;
		this.billingAlign = billingAlign;
		this.createdById = clientId;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.lastModifiedById = lastModifiedById;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public Date getNextBillableDay() {
		return nextBillableDay;
	}

	public void setNextBillableDay(Date nextBillableDay) {
		this.nextBillableDay = nextBillableDay;
	}

	public String getBillingAlign() {
		return billingAlign;
	}

	public void setBillingAlign(String billingAlign) {
		this.billingAlign = billingAlign;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getLastModifiedById() {
		return lastModifiedById;
	}

	public void setLastModifiedById(Long lastModifiedById) {
		this.lastModifiedById = lastModifiedById;
	}

}
