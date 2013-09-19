package org.mifosplatform.billing.inventory.data;

import org.joda.time.LocalDate;

public class InventoryItemDetailsData {
	
	
	
	private Long id;
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
	private Long createdById;
	private LocalDate createdDate;
	private LocalDate lastModifiedDate;
	private Long lastModifiedDateById;
	private String itemDescription;
	private String supplier;
	
	public InventoryItemDetailsData(Long id,Long itemMasterId,String serialNumber,Long grnId,String provisioningSerialNumber,String quality,String status,Long warranty,String remarks){
		
		this.id=id;
		this.itemMasterId=itemMasterId;
		this.serialNumber=serialNumber;
		this.grnId=grnId;
		this.provisioningSerialNumber=provisioningSerialNumber;
		this.quality=quality;
		this.status=status;
		this.warranty=warranty;
		this.remarks=remarks;
		
	}
	
	public InventoryItemDetailsData(Long id,Long itemMasterId,String serialNumber,Long grnId,String provisioningSerialNumber,String quality,String status,Long warranty,String remarks, String itemDescription, final String supplier, final Long clientId){
		
		this.id=id;
		this.itemMasterId=itemMasterId;
		this.serialNumber=serialNumber;
		this.grnId=grnId;
		this.provisioningSerialNumber=provisioningSerialNumber;
		this.quality=quality;
		this.status=status;
		this.warranty=warranty;
		this.remarks=remarks;
		this.itemDescription = itemDescription;
		this.supplier = supplier;
		this.clientId = clientId;
		
	}
	
	public InventoryItemDetailsData(Long id,Long itemMasterId,String serialNumber,Long grnId,String provisioningSerialNumber,String quality,String status,Long warranty,String remarks, String itemDescription){
		
		this.id=id;
		this.itemMasterId=itemMasterId;
		this.serialNumber=serialNumber;
		this.grnId=grnId;
		this.provisioningSerialNumber=provisioningSerialNumber;
		this.quality=quality;
		this.status=status;
		this.warranty=warranty;
		this.remarks=remarks;
		this.itemDescription = itemDescription;
		
	}
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
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



	public Long getCreatedById() {
		return createdById;
	}



	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}



	public LocalDate getCreatedDate() {
		return createdDate;
	}



	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}



	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}



	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}



	public Long getLastModifiedDateById() {
		return lastModifiedDateById;
	}



	public void setLastModifiedDateById(Long lastModifiedDateById) {
		this.lastModifiedDateById = lastModifiedDateById;
	}



	public InventoryItemDetailsData(){}

	/**
	 * @return the itemDescription
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * @param itemDescription the itemDescription to set
	 */
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	
}
