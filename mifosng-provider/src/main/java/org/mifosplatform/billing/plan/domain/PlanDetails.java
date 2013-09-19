package org.mifosplatform.billing.plan.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.api.JsonCommand;

@Entity
@Table(name = "b_plan_detail")
public class PlanDetails {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@ManyToOne
    @JoinColumn(name="plan_id")
    private Plan plan;

	@Column(name ="service_code", length=50)
    private String serviceCode;


	@Column(name = "is_deleted", nullable = false)
	private char isDeleted = 'n';


	public PlanDetails()
	{}
	public PlanDetails(final String serviceCode)
	{

		this.serviceCode=serviceCode;
		//this.is_deleted=null;
		this.plan=null;

	}


	public String getServiceCode() {
		return serviceCode;
	}


	public char isIsDeleted() {
		return isDeleted;
	}



	public Plan getPlan() {
		return plan;
	}

	public void update(Plan plan1)
	{
		this.plan=plan1;
	}
	public void delete() {
		this.isDeleted='y';

	}
	public static PlanDetails fromJson(JsonCommand command) {
		
		    final String serviceCode = command.stringValueOfParameterNamed("serviceCode");
		    return new PlanDetails(serviceCode);
		
	}
	public void update(JsonCommand command) {
		// TODO Auto-generated method stub
		
	}



}