package org.mifosplatform.billing.eventorder.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.billing.order.data.OrderStatusEnumaration;
import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;

@Entity
@Table(name = "b_prepare_request")
public class PrepareRequest extends AbstractAuditableCustom<AppUser, Long>{

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "status")
	private String status;

	@Column(name = "request_type")
	private String requestType;

	

	@Column(name = "is_provisioning")
	private char isProvisioning;
	
	@Column(name = "provisioning_sys")
	private String provisioningSystem;
	
	@Column(name = "plan_name")
	private String planCode;
	
	
	 public  PrepareRequest() {
		// TODO Auto-generated constructor stub
	}


	public PrepareRequest(Long clientId, Long orderId, String requstStatus,	String provisioningSystem, char isProvisioning,
			String generateRequest, String planCode) {
		this.clientId=clientId;
		this.orderId=orderId;
		this.status=OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.INACTIVE).getValue();
		this.isProvisioning=provisioningSystem.equalsIgnoreCase("NONE")?'Y':'N';
		this.requestType=requstStatus;
		this.planCode=planCode;
		this.provisioningSystem=provisioningSystem;
		
		
              
	}



	public Long getClientId() {
		return clientId;
	}


	public Long getOrderId() {
		return orderId;
	}


	public String getStatus() {
		return status;
	}


	public String getRequestType() {
		return requestType;
	}


	


	public char getIsProvisioning() {
		return isProvisioning;
	}


	public void updateProvisioning() {
		this.isProvisioning='Y';
		this.status=StatusTypeEnum.ACTIVE.toString();
		
	}


	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}


	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}


	/**
	 * @param isProvisioning the isProvisioning to set
	 */
	public void setIsProvisioning(char isProvisioning) {
		this.isProvisioning = isProvisioning;
	}


	/**
	 * @param provisioningSystem the provisioningSystem to set
	 */
	public void setProvisioningSystem(String provisioningSystem) {
		this.provisioningSystem = provisioningSystem;
	}
	
	

		
	 
	 
			
	}
 
	
	
