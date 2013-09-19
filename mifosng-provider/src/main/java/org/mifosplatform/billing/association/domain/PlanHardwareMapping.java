package org.mifosplatform.billing.association.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@Entity
@Table(name = "b_hw_plan_mapping")
public class PlanHardwareMapping extends AbstractPersistable<Long> {

	@Column(name = "item_code")
	private String itemCode;

	@Column(name = "plan_code")
	private String planCode;
	
	 public PlanHardwareMapping() {
		// TODO Auto-generated constructor stub
			
	}

	public String getItemCode() {
		return itemCode;
	}

	public String getPlanCode() {
		return planCode;
	}


	
}
	