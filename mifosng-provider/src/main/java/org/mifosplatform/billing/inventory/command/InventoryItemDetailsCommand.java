package org.mifosplatform.billing.inventory.command;

import java.util.Set;


public class InventoryItemDetailsCommand {

	/*item_master_id` bigint(20) NOT NULL,
	  `serial_no` varchar(100) DEFAULT NULL,  
	  `grn_id` bigint(20) NOT NULL,
	  `provisioning_serialno` varchar(100) DEFAULT NULL,   
	  `quality` varchar(20) DEFAULT NULL,    -- Enum Good, Faulty and Scrap
	  `status` varchar(20) DEFAULT NULL,     -- Enum Used
	  `office_id` bigint(20) NOT NULL,
	  `client_id` bigint(20) NOT NULL,
	  `warranty` bigint(20) DEFAULT NULL,
	  `remarks` varchar(100) DEFAULT NULL,
	
	*/
	
	private Long itemMasterId;
	private String serialNumber;
	private Long grnId;
	private String provisioningSerialNumber;
	private String quality;
	private String status;
	private Long officeId;
	private Long clientId;
	private Long warranty;
	private String remarks;
	private final Set<String> modifiedParameters;
	

	
	public InventoryItemDetailsCommand(Long itemMasterId,String serialNumber,Long grnId,String provisiningSerialNumber,String quality,String status,Long warranty, String remarks,Set<String> modifiedParameters){
		this.itemMasterId=itemMasterId;
		this.serialNumber=serialNumber;
		this.grnId=grnId;
		this.provisioningSerialNumber=provisiningSerialNumber;
		this.quality=quality;
		this.status=status;
		this.warranty=warranty;
		this.remarks=remarks;
		this.modifiedParameters=modifiedParameters;
		
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

	
	public Set<String> getModifiedParameters() {
		return modifiedParameters;
	}  
}
