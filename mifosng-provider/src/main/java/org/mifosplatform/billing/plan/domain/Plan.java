package org.mifosplatform.billing.plan.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;

@Entity
@Table(name = "b_plan_master", uniqueConstraints = @UniqueConstraint(name = "plan_code_key", columnNames = { "plan_code" }))
public class Plan{


	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@Column(name = "plan_code", length = 65536)
	private String planCode;

	@Column(name = "plan_description")
	private String description;

	@Column(name = "plan_status")
	private Long status;
	
	@Column(name = "duration")
	private String contractPeriod;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "bill_rule")
	private Long billRule;

	
	@Column(name = "is_deleted", nullable = false)
	private char deleted='n';
	
	@Column(name = "is_prepaid", nullable = false)
	private char isPrepaid='N';
	
	@Column(name = "allow_topup", nullable = false)
	private char allowTopup='N';
	
	@Column(name = "is_hw_req", nullable = false)
	private char isHwReq='Y';

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "plan", orphanRemoval = true)
	private List<PlanDetails> details = new ArrayList<PlanDetails>();
	
	@Column(name = "provision_sys")
	private String provisionSystem;
    

	
	public Plan() {
		// TODO Auto-generated constructor stub
	}

	public Plan(final String code, final String description,final LocalDate start_date, final LocalDate endDate,
			final Long bill_rule, final Long status,final String contractPeriod,
			final List<PlanDetails> details, String provisioingSystem, boolean isPrepaid, boolean allowTopup) {
		this.planCode = code;
		this.description = description;
		if (endDate != null)
			this.endDate = endDate.toDate();
		this.startDate = start_date.toDate();
		this.status = status;
		this.contractPeriod = contractPeriod;
		this.billRule = bill_rule;
	//	this.details = details;
		this.provisionSystem=provisioingSystem;
		this.isPrepaid=isPrepaid?'Y':'N';
		this.allowTopup=allowTopup?'Y':'N';

	}

	public List<PlanDetails> getDetails() {
		return details;
	}

	public String getCode() {
		return planCode;
	}

	public String getDescription() {
		return description;
	}

	public Long getStatus() {
		return status;
	}

	public Date getStart_date() {
		return startDate;
	}

	public Date getEnd_date() {
		return endDate;
	}



	public Long getId() {
		return id;
	}

	public String getPlanCode() {
		return planCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Long getBillRule() {
		return billRule;
	}

	public char isDeleted() {
		return deleted;
	}

	public String getContractPeriod() {
		return contractPeriod;
	}

	public void addServieDetails(PlanDetails serviceDetail) {

		serviceDetail.update(this);
		this.details.add(serviceDetail);

	}

	public Long getBill_rule() {
		return billRule;
	}

	

	

	public char isHardwareReq() {
		return isHwReq;
	}

	public char getDeleted() {
		return deleted;
	}

	public String getProvisionSystem() {
		return provisionSystem;
	}

	public void delete() {

		/*this.code = data.getPlan_code();
		this.description = data.getPlan_description();

		if (data.getEndDate() != null)
			this.end_date = data.getEndDate().toDate();
		this.start_date = data.getStartDate().toDate();
		this.status = data.getStatus();
		this.contract_period = data.getPeriod();
		this.bill_rule = data.getBillRule();*/

		if (deleted =='y') {

		} else {
			this.deleted = 'y';
			this.planCode=this.planCode+"_"+this.getId()+"_DELETED";

		}


	}

	public Map<String, Object> update(JsonCommand command) {
		 final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);
		  final String firstnameParamName = "planCode";
	        if (command.isChangeInStringParameterNamed(firstnameParamName, this.planCode)) {
	            final String newValue = command.stringValueOfParameterNamed(firstnameParamName);
	            actualChanges.put(firstnameParamName, newValue);
	            this.planCode = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String descriptionParamName = "planDescription";
	        if (command.isChangeInStringParameterNamed(descriptionParamName, this.description)) {
	            final String newValue = command.stringValueOfParameterNamed(descriptionParamName);
	            actualChanges.put(firstnameParamName, newValue);
	            this.description = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        
	        final String provisioingSystem = "provisioingSystem";
	        if (command.isChangeInStringParameterNamed(provisioingSystem, this.provisionSystem)) {
	            final String newValue = command.stringValueOfParameterNamed(provisioingSystem);
	            actualChanges.put(provisioingSystem, newValue);
	            this.provisionSystem = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String startDateParamName = "startDate";
			if (command.isChangeInLocalDateParameterNamed(startDateParamName,
					new LocalDate(this.startDate))) {
				final LocalDate newValue = command
						.localDateValueOfParameterNamed(startDateParamName);
				actualChanges.put(startDateParamName, newValue);
			}
			
			 final String endDateParamName = "endDate";
				if (command.isChangeInLocalDateParameterNamed(endDateParamName,
						new LocalDate(this.endDate))) {
					final LocalDate newValue = command
							.localDateValueOfParameterNamed(endDateParamName);
					actualChanges.put(endDateParamName, newValue);
				}
	        
				  
		      
		        
		        final String billRuleParamName = "billRule";
				if (command.isChangeInLongParameterNamed(billRuleParamName,
						this.billRule)) {
					final Long newValue = command
							.longValueOfParameterNamed(billRuleParamName);
					actualChanges.put(billRuleParamName, newValue);
					this.billRule=newValue;
				}
				

		        final String statusParamName = "status";
				if (command.isChangeInLongParameterNamed(statusParamName,
						this.status)) {
					final Long newValue = command
							.longValueOfParameterNamed(statusParamName);
					actualChanges.put(statusParamName, newValue);
					this.status=newValue;
				}
				
				 
				  boolean isPrepaid=command.booleanPrimitiveValueOfParameterNamed("isPrepaid");
				  final char isPrepaidParamName =isPrepaid?'Y':'N';
						this.isPrepaid=isPrepaidParamName;
						
						  final String contractPeriodParamName = "duration";
					        if (command.isChangeInStringParameterNamed(contractPeriodParamName, this.contractPeriod)) {
					            String newValue = command.stringValueOfParameterNamed(contractPeriodParamName);
					               newValue=this.isPrepaid == 'Y'?newValue:null;
					            actualChanges.put(contractPeriodParamName, newValue);
					            this.contractPeriod = StringUtils.defaultIfEmpty(newValue, null);
					        
						}
					  final boolean allowTopupParamName =command.booleanPrimitiveValueOfParameterNamed("allowTopup");
						
							this.allowTopup=allowTopupParamName?'Y':'N';
						
	        return actualChanges;

	}

	public static Plan fromJson(JsonCommand command) {
		 final String planCode = command.stringValueOfParameterNamed("planCode");
		    final String planDescription = command.stringValueOfParameterNamed("planDescription");
		    final LocalDate startDate = command.localDateValueOfParameterNamed("startDate");
		    final LocalDate endDate = command.localDateValueOfParameterNamed("endDate");
		    final Long status = command.longValueOfParameterNamed("status");
		    final String duration = command.stringValueOfParameterNamed("duration");
		    final Long billRule = command.longValueOfParameterNamed("billRule");
		    final String provisioingSystem=command.stringValueOfParameterNamed("provisioingSystem");
		    boolean isPrepaid=command.booleanPrimitiveValueOfParameterNamed("isPrepaid");
		    boolean allowTopup=command.booleanPrimitiveValueOfParameterNamed("allowTopup");
		   
		    
		return new Plan(planCode,planDescription,startDate,endDate,billRule,status,duration,null,provisioingSystem,isPrepaid,allowTopup);
	}

	/**
	 * @return the isPrepaid
	 */
	public char isPrepaid() {
		return isPrepaid;
	}

	/**
	 * @return the allowTopup
	 */
	public char getAllowTopup() {
		return allowTopup;
	}
		
	}

	

	

