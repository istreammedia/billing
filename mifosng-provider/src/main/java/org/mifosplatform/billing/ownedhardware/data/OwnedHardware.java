package org.mifosplatform.billing.ownedhardware.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;


@Entity
@Table(name="b_owned_hardware")
public class OwnedHardware extends AbstractAuditableCustom<AppUser, Long>{

	
	@Column(name="client_id")
	private Long clientId;
	
	@Column(name="serial_number")
	private String serialNumber;
	
	@Column(name="provisioning_serial_number")
	private String provisioningSerialNumber;
	
	@Column(name="allocated_date")
	private Date allocatedDate;
	
	@Column(name="status")
	private String status;
	
	@Column(name="item_type")
	private String itemType;
	
	public OwnedHardware() {
		
	}

	private OwnedHardware(Long clientId, String serialNumber, String provisioningSerialNumber, Date allocatedDate, String status, String itemType){
		
		this.clientId = clientId;
		this.serialNumber = serialNumber;
		this.provisioningSerialNumber = provisioningSerialNumber;
		this.allocatedDate = allocatedDate;
		this.status = status;
		this.itemType = itemType;
	
	}
	

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getProvisioningSerialNumber() {
		return provisioningSerialNumber;
	}

	public void setProvisioningSerialNumber(String provisioningSerialNumber) {
		this.provisioningSerialNumber = provisioningSerialNumber;
	}

	public Date getAllocatedDate() {
		return allocatedDate;
	}

	public void setAllocatedDate(Date allocatedDate) {
		this.allocatedDate = allocatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	
	public static OwnedHardware fromJson(JsonCommand command,Long clientId){
		String serialNumber = command.stringValueOfParameterNamed("serialNumber");
		String provisioningSerialNumber = command.stringValueOfParameterNamed("provisioningSerialNumber");
		Date allocatedDate = command.DateValueOfParameterNamed("allocationDate");
		
		String itemType = command.stringValueOfParameterNamed("itemType");
		return new OwnedHardware(clientId, serialNumber, provisioningSerialNumber, allocatedDate, "ACTIVE", itemType); 
	}
}
