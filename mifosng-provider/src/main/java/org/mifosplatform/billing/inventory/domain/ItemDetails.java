package org.mifosplatform.billing.inventory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;


@Entity
@Table(name="b_item_detail")
public class ItemDetails extends AbstractAuditableCustom<AppUser, Long>{

	
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

	public ItemDetails(){}
	
	
	public ItemDetails(Long itemMasterId,String serialNumber,Long grnId,String provisioningSerialNumber,String quality,String status,Long officeId,Long clientId,Long warranty,String remarks){
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
	
	public static ItemDetails create(Long itemMasterId,String serialNumber,Long grnId,String provisioningSerialNumber,String quality,String status,Long officeId,Long clientId,Long warranty,String remarks){
		
		return new ItemDetails(itemMasterId,serialNumber,grnId,provisioningSerialNumber,quality,status,officeId,clientId,warranty,remarks);
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
	
	
	
	
}
