package org.mifosplatform.billing.plan.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "b_plan_hw")
public class PlanHardWareDetails{


	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@Column(name = "plan_id")
	private Long planId;

	@Column(name = "hw_type")
	private String hardWareType;
	
	@Column(name = "plan_code")
	private String planCode;
	
	@Column(name = "is_hw_required")
	private char isHardWareReq='N';
	
	
    

	
	public PlanHardWareDetails() {
		// TODO Auto-generated constructor stub
	}





	public PlanHardWareDetails(Long planId, char isHardWareReq, String hardWareType, String planCode) {
		this.planId=planId;
		this.isHardWareReq=isHardWareReq;
		this.hardWareType=hardWareType;
		this.planCode=planCode;
	}

}