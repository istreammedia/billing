package org.mifosplatform.billing.inventory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.useradministration.domain.AppUser;

import com.google.gson.JsonElement;


@Entity
@Table(name = "b_item_detail")//, uniqueConstraints = @UniqueConstraint(name = "serial_no_constraint", columnNames = { "serial_no" }))
public class InventoryItemDetails extends AbstractAuditableCustom<AppUser, Long>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2876090423570296480L;

	@Column(name="item_master_id", nullable=false, length=20)
	private Long itemMasterId;
	
	@Column(name="serial_no", nullable=true, length=100)
	private String serialNumber;
	
	@Column(name="grn_id", nullable=false, length=20)
	private Long grnId;
	
	@Column(name="provisioning_serialno",nullable=true,length=100)
	private String provisioningSerialNumber;
	
	@Column(name="quality",nullable=true,length=20)
	private String quality;
	
	@Column(name="status",nullable=true,length=20)
	private String status;
	
	@Column(name="office_id",nullable=false, length=20)
	private Long officeId;
	
	@Column(name="client_id",nullable=false,length=20)
	private Long clientId;
	
	@Column(name="warranty",nullable=true,length=20)
	private Long warranty;
	
	@Column(name="remarks",nullable=true,length=100)
	private String remarks;



	public InventoryItemDetails(){}
	
	
	public InventoryItemDetails(Long itemMasterId,String serialNumber,Long grnId,String provisioningSerialNumber,String quality,String status,Long warranty,String remarks){
		this.itemMasterId=itemMasterId;
		this.serialNumber=serialNumber;
		this.grnId=grnId;
		this.provisioningSerialNumber=provisioningSerialNumber;
		this.quality=quality;
		this.status=status;
		this.warranty=warranty;
		this.remarks=remarks;
		
		
	}
	
	
	public InventoryItemDetails(Long itemMasterId,String serialNumber,Long grnId,String provisioningSerialNumber,String quality,String status,Long officeId,Long clientId,Long warranty,String remarks){
		this.itemMasterId=itemMasterId;
		this.serialNumber=serialNumber;
		this.grnId=grnId;
		this.provisioningSerialNumber=provisioningSerialNumber;
		this.quality=quality;
		this.status=status;
		this.officeId=officeId;
		this.clientId=clientId;
		this.warranty=warranty;
		this.remarks=remarks;
		
		
	}
	
	
	
	public InventoryItemDetails(Long id) {
		this.itemMasterId = id;
	}
	
	public InventoryItemDetails(final Long id,final Long clientId) {
		this.itemMasterId = id;
		this.clientId = clientId;
	}



	public Long getItemMasterId() {
		return itemMasterId;
	}

	public void setItemMasterId(Long itemMasterId) {
		this.itemMasterId = itemMasterId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Long getGrnId() {
		return grnId;
	}

	public void setGrnId(Long grnId) {
		this.grnId = grnId;
	}

	public String getProvisioningSerialNumber() {
		return provisioningSerialNumber;
	}

	public void setProvisioningSerialNumber(String provisioningSerialNumber) {
		this.provisioningSerialNumber = provisioningSerialNumber;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getWarranty() {
		return warranty;
	}

	public void setWarranty(Long warranty) {
		this.warranty = warranty;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	public static InventoryItemDetails fromJson(JsonCommand command, FromJsonHelper fromJsonHelper){
		
		//final JsonElement element = fromJsonHelper.parse(command.toString());
		Integer item = command.integerValueSansLocaleOfParameterNamed("itemMasterId");
		Long itemMasterId = item.longValue();
		
		//Long warranty = w.longValue();
		String remarks = command.stringValueOfParameterNamed("remarks");
		Long grnId = command.longValueOfParameterNamed("grnId");
		String serialNumber = command.stringValueOfParameterNamed("serialNumber");
		String provisioningSerialNumber = command.stringValueOfParameterNamed("provisioningSerialNumber");
		String quality = command.stringValueOfParameterNamed("quality");
		String status = command.stringValueOfParameterNamed("status");
		return new InventoryItemDetails(itemMasterId,serialNumber,grnId,provisioningSerialNumber,quality,status,null,remarks);
	}
	
}
