package org.mifosplatform.billing.plan.data;

import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class PlanData {

	private  Long id;
	private  Long billRule;
	private  String planCode;
	private  String planDescription;
	private LocalDate startDate;
	private  LocalDate endDate;
	private  Long status;
	private EnumOptionData planstatus;
	private  String serviceDescription;
	private  List<ServiceData> services;
	private  List<ServiceData> selectedServices;
	private List<String> contractPeriods;
	private List<SubscriptionData> subscriptiondata;
	private List<BillRuleData> billRuleDatas;
	private List<EnumOptionData> planStatus,volumeTypes;
	private  String contractPeriod;
	private PlanData datas;
	private long statusname;
	private List<SystemData> provisionSysData;
	private String provisionSystem;
	private String isPrepaid;
	private String allowTopup;
	private String volume;
	private String units;
	private String unitType;
	
	private Boolean isActive=false;
	private Integer planCount = 0;
	private List<PlanData> data = null;
	
	public PlanData(List<ServiceData> data, List<BillRuleData> billData,List<SubscriptionData> contractPeriod, List<EnumOptionData> status,
			PlanData datas, List<ServiceData> selectedservice,List<SystemData> provisionSysData, List<EnumOptionData> volumeType) {
		if(datas!=null){
		this.id = datas.getId();
		this.planCode = datas.getplanCode();
		this.subscriptiondata = contractPeriod;
		this.startDate = datas.getStartDate();
		this.status = datas.getStatus();
		this.billRule = datas.getBillRule();
		this.endDate = datas.getEndDate();
		this.planDescription = datas.getplanDescription();
		this.provisionSystem=datas.getProvisionSystem();
		this.isPrepaid=datas.getIsPrepaid();
		this.allowTopup=datas.getAllowTopup();
		this.volume=datas.getVolume();
		this.units=datas.getUnits();
		this.unitType=datas.getUnitType();
		this.contractPeriod=datas.getPeriod();
		}
		this.services = data;
        this.provisionSysData=provisionSysData;
		this.selectedServices = selectedservice;
		this.billRuleDatas = billData;
		this.subscriptiondata=contractPeriod;
		this.planStatus = status;
		this.serviceDescription = null;
		
		this.datas = datas;
		this.datas = null;
		this.volumeTypes=volumeType;

	}

	
	public PlanData(Long id, String planCode, LocalDate startDate,LocalDate endDate, Long bill_rule, String contractPeriod,
			long status, String planDescription, long status1,String provisionSys,EnumOptionData enumstatus, String isPrepaid,
			String allowTopup, String volume, String units, String unitType) {

		this.id = id;
		this.planCode = planCode;
		this.serviceDescription = null;
		this.startDate = startDate;
		this.status = status;
		this.billRule = bill_rule;
		this.endDate = endDate;
		this.planDescription = planDescription;
		this.services = null;
		this.billRuleDatas = null;
		this.contractPeriod = contractPeriod;
        this.provisionSystem=provisionSys;  
		this.selectedServices = null;
		this.statusname = status1;
		this.planstatus = enumstatus;
		this.isPrepaid=isPrepaid;
		this.allowTopup=allowTopup;
		this.volume=volume;
		this.units=units;
		this.unitType=unitType;
	}

	

	public PlanData(List<PlanData> datas) {
		this.data = datas;
	}





	public PlanData(Long id, String planCode, String planDescription) {
              this.id=id;
              this.planCode=planCode;
              this.planDescription=planDescription;
	
	}


	public String getProvisionSystem() {
		return provisionSystem;
	}


	public EnumOptionData getPlanstatus() {
		return planstatus;
	}

	public PlanData getDatas() {
		return datas;
	}

	public List<ServiceData> getSelectedServices() {
		return selectedServices;
	}

	public long getStatusname() {
		return statusname;
	}

	public List<EnumOptionData> getPlanStatus() {
		return planStatus;
	}

	public String getPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(List<String> contractPeriod) {
		this.contractPeriods = contractPeriod;
	}

	public List<BillRuleData> getBillRuleData() {
		return billRuleDatas;
	}

	public Long getId() {
		return id;
	}

	public String getplanCode() {
		return planCode;
	}

	public String getplanDescription() {
		return planDescription;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public Long getStatus() {
		return status;
	}

	public List<ServiceData> getServicedata() {
		return services;
	}

	public Long getBillRule() {
		return billRule;
	}

	public List<String> getContractPeriod() {
		return contractPeriods;
	}

	public String getserviceDescription() {
		return serviceDescription;
	}

	public List<SubscriptionData> getSubscriptiondata() {
		return subscriptiondata;
	}


	/**
	 * @return the planCode
	 */
	public String getPlanCode() {
		return planCode;
	}


	/**
	 * @return the planDescription
	 */
	public String getPlanDescription() {
		return planDescription;
	}


	/**
	 * @return the serviceDescription
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}


	/**
	 * @return the services
	 */
	public List<ServiceData> getServices() {
		return services;
	}


	/**
	 * @return the billRuleDatas
	 */
	public List<BillRuleData> getBillRuleDatas() {
		return billRuleDatas;
	}


	/**
	 * @return the volumeTypes
	 */
	public List<EnumOptionData> getVolumeTypes() {
		return volumeTypes;
	}


	/**
	 * @return the provisionSysData
	 */
	public List<SystemData> getProvisionSysData() {
		return provisionSysData;
	}


	/**
	 * @return the isPrepaid
	 */
	public String getIsPrepaid() {
		return isPrepaid;
	}


	/**
	 * @return the allowTopup
	 */
	public String getAllowTopup() {
		return allowTopup;
	}


	/**
	 * @return the volume
	 */
	public String getVolume() {
		return volume;
	}


	/**
	 * @return the units
	 */
	public String getUnits() {
		return units;
	}


	/**
	 * @return the unitType
	 */
	public String getUnitType() {
		return unitType;
	}


	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}


	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}


	/**
	 * @return the planCount
	 */
	public Integer getPlanCount() {
		return planCount;
	}


	/**
	 * @param planCount the planCount to set
	 */
	public void setPlanCount(Integer planCount) {
		this.planCount = planCount;
	}
	
	

}
