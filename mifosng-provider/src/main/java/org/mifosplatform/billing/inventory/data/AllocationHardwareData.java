package org.mifosplatform.billing.inventory.data;

import java.util.List;

public class AllocationHardwareData {

	private List<InventoryItemSerialNumberData> serialNumbers;
	private Long quantity;
	private Long itemMasterId;
	
	public AllocationHardwareData() {}
	
	public AllocationHardwareData(List<InventoryItemSerialNumberData> serialNumbers,Long quantity,Long itemMasterId){
		this.serialNumbers = serialNumbers;
		this.quantity = quantity;
		this.itemMasterId = itemMasterId;
	}
	
	
	public List<InventoryItemSerialNumberData> getSrialNumbers() {
		return serialNumbers;
	}
	public void setSrialNumbers(
			List<InventoryItemSerialNumberData> srialNumbers) {
		this.serialNumbers = srialNumbers;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Long getItemMasterId() {
		return itemMasterId;
	}
	public void setItemMasterId(Long itemMasterId) {
		this.itemMasterId = itemMasterId;
	}
	
	
}
