package org.mifosplatform.billing.plan.data;

import java.util.Date;
import java.util.List;

import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class SinglePlandata  {
	private final Long id;
	private final Long billRule;

	private final String plan_code;
	private final String plan_description;
	private final Date startDate;
	private final Date endDate;
	private final String status;
	private final String service_description;
	private List<ServiceData> servicedata;
	private List<String> contractPeriod;
	private List<SubscriptionData> subscriptiondata;
	private List<BillRuleData> statusType;
	private List<EnumOptionData> planStatus;
	private final String Period;
	private boolean billingcycle;



	public SinglePlandata(Long id, String plan_code, Date startDate,
			Date endDate, Long bill_rule, String contractPeriod,
			boolean isanyday_bill, String plan_status, String plan_description) {
		this.id=id;
		this.plan_code=plan_code;
		this.service_description=null;
		this.startDate=startDate;
		this.status=plan_status;
		this.billRule=bill_rule;
		this.endDate=endDate;
		this.plan_description=plan_description;
		this.servicedata=null;
		this.statusType=null;
		this.Period=contractPeriod;
		this.billingcycle=isanyday_bill;


	}



	public List<ServiceData> getServicedata() {
		return servicedata;
	}



	public void setServicedata(List<ServiceData> servicedata) {
		this.servicedata = servicedata;
	}



	public List<String> getContractPeriod() {
		return contractPeriod;
	}



	public void setContractPeriod(List<String> contractPeriod) {
		this.contractPeriod = contractPeriod;
	}



	public List<SubscriptionData> getSubscriptiondata() {
		return subscriptiondata;
	}



	public void setSubscriptiondata(List<SubscriptionData> subscriptiondata) {
		this.subscriptiondata = subscriptiondata;
	}



	public List<BillRuleData> getStatusType() {
		return statusType;
	}



	public void setStatusType(List<BillRuleData> statusType) {
		this.statusType = statusType;
	}



	public List<EnumOptionData> getPlanStatus() {
		return planStatus;
	}



	public void setPlanStatus(List<EnumOptionData> planStatus) {
		this.planStatus = planStatus;
	}



	public boolean isBillingcycle() {
		return billingcycle;
	}



	public void setBillingcycle(boolean billingcycle) {
		this.billingcycle = billingcycle;
	}



	public Long getId() {
		return id;
	}



	public Long getBillRule() {
		return billRule;
	}



	public String getPlan_code() {
		return plan_code;
	}



	public String getPlan_description() {
		return plan_description;
	}



	public Date getStartDate() {
		return startDate;
	}



	public Date getEndDate() {
		return endDate;
	}



	public String getStatus() {
		return status;
	}



	public String getService_description() {
		return service_description;
	}



	public String getPeriod() {
		return Period;
	}



}
