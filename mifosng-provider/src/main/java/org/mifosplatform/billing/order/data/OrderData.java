package org.mifosplatform.billing.order.data;

import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.billing.paymode.data.McodeData;
import org.mifosplatform.billing.payterms.data.PaytermData;
import org.mifosplatform.billing.plan.data.PlanCodeData;

public class OrderData {
	private Long id;
	private Long pdid;
	private Long pcid;
	private Long clientId;
	private String service_code;
	private String plan_code;
	private String chargeCode;
	private double price;
	private String variant;
	private String status;
	private Long period;
	private LocalDate startDate;
	private LocalDate currentDate;
	private LocalDate endDate;
	private String billingFrequency;
	private List<PlanCodeData> plandata;
	private List<PaytermData> paytermdata;
	private List<SubscriptionData> subscriptiondata;
	private List<OrderPriceData> orderPriceData;
	private String cancelledStatus;
	private String contractPeriod;
	private boolean flag;
	private Collection<McodeData> disconnectDetails;
	private List<OrderHistoryData> orderHistory;
	private String isPrepaid;
	private String allowtopup;
	private List<OrderData> clientOrders;
	private String userAction;

	public OrderData(List<PlanCodeData> allowedtypes,
			List<PaytermData> paytermData,
			List<SubscriptionData> contractPeriod, OrderData data) {

		if (data != null) {
			this.id = data.getId();
			this.pdid = data.getPdid();
			this.plan_code = data.getPlan_code();
			this.status = null;
			this.period = data.getPeriod();
			this.pcid = data.getPcid();
			this.service_code = null;

			this.startDate = data.getStartDate();
		}
		this.startDate=new LocalDate();
		this.variant = null;
		this.chargeCode = null;
		this.paytermdata = paytermData;
		this.plandata = allowedtypes;
		this.subscriptiondata = contractPeriod;

	}

	public OrderData(Long id, Long planId, String plancode, String status,
			LocalDate startDate, LocalDate endDate, double price,
			String contractPeriod, String isprepaid, String allowtopup,String userAction) {
		this.id = id;
		this.pdid = planId;
		this.plan_code = plancode;
		this.status = status;
		this.cancelledStatus = "DISCONNECTED";
		this.period = null;
		this.startDate = startDate;
		this.currentDate = new LocalDate();
		this.endDate = endDate;
		this.pcid = null;
		this.service_code = null;
		this.price = price;
		this.variant = null;
		this.chargeCode = null;
		this.paytermdata = null;
		this.plandata = null;
		this.subscriptiondata = null;
		this.contractPeriod = contractPeriod;
		this.isPrepaid=isprepaid;
		this.allowtopup=allowtopup;
		this.userAction=userAction;

		if (startDate.equals(currentDate)) {
			this.flag = true;
		}

	}

	public OrderData(List<OrderPriceData> priceDatas, List<OrderHistoryData> historyDatas) {
		this.orderPriceData = priceDatas;
		this.orderHistory=historyDatas;

	}

	public OrderData(Collection<McodeData> disconnectDetails,List<SubscriptionData> subscriptionDatas) {
		this.disconnectDetails=disconnectDetails;
		this.subscriptiondata=subscriptionDatas;
	}

	public OrderData(Long clientId, List<OrderData> clientOrders) {
		this.clientId=clientId;
		this.clientOrders=clientOrders;
	}

	public Long getId() {
		return id;
	}

	public Long getPdid() {
		return pdid;
	}

	public Long getClientId() {
		return clientId;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}

	public List<OrderPriceData> getOrderPriceData() {
		return orderPriceData;
	}

	public Long getPcid() {
		return pcid;
	}

	public String getService_code() {
		return service_code;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public String getStatus() {
		return status;
	}

	public Long getPeriod() {
		return period;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public String getPlan_code() {
		return plan_code;
	}

	public double getPrice() {
		return price;
	}

	public String getVariant() {
		return variant;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public List<PlanCodeData> getPlandata() {
		return plandata;
	}

	public List<PaytermData> getPaytermdata() {
		return paytermdata;
	}

	public List<SubscriptionData> getSubscriptiondata() {
		return subscriptiondata;
	}

	public void setPaytermData(List<PaytermData> data) {
		this.paytermdata = data;
	}

	public void setDisconnectDetails(Collection<McodeData> disconnectDetails) {
	this.disconnectDetails=disconnectDetails;
		
	}

	public void setDuration(String duration) {
		this.contractPeriod=duration;
		
	}

	public void setplanType(String planType) {
	this.isPrepaid=planType;
		
	}
}
