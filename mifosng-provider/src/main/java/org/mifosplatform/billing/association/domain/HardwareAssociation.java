package org.mifosplatform.billing.association.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;

@SuppressWarnings("serial")
@Entity
@Table(name = "b_association")
public class HardwareAssociation extends AbstractAuditableCustom<AppUser, Long> {

	

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "plan_id")
	private Long planId;
	


	@Column(name = "hw_serial_no")
	private String serialNo;
	
	@Column(name = "order_id")
	private Long orderId;

	
	 public HardwareAssociation() {
		// TODO Auto-generated constructor stub
			
	}


	public HardwareAssociation(Long clientId, Long planId, String serialNo,Long orderId) {
            this.clientId=clientId;
            this.planId=planId;
            this.serialNo=serialNo;
            this.orderId=orderId;
	
	
	}
}
	