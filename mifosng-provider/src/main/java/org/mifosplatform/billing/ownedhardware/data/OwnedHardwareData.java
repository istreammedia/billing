package org.mifosplatform.billing.ownedhardware.data;

import org.joda.time.LocalDate;


public class OwnedHardwareData {
	
	
	
	private Long id;
	private Long clientId;
	private String serialNumber;
	private String provisioningSerialNumber;
	private LocalDate allocationDate;
	private String status;
	private String itemType;
	
	
	
	public OwnedHardwareData() {
		
	}
	
	
	public OwnedHardwareData(Long id, Long clientId, String serialNumber, String provisioningSerialNumber, LocalDate allocationDate, String status, String itemType){
		this.id = id;
		this.clientId = clientId;
		this.serialNumber = serialNumber;
		this.provisioningSerialNumber = provisioningSerialNumber;
		this.allocationDate = allocationDate;
		this.status = status;
		this.itemType = itemType;
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public LocalDate getAllocationDate() {
		return allocationDate;
	}
	public void setAllocationDate(LocalDate allocationDate) {
		this.allocationDate = allocationDate;
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
	
	
}
