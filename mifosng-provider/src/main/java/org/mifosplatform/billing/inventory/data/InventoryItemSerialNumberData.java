package org.mifosplatform.billing.inventory.data;

import java.util.List;


public class InventoryItemSerialNumberData {

	private String serialNumbers;

	private List<String> serials;
	private Long quantity;
	private Long itemMasterId;
	
	public InventoryItemSerialNumberData(List<String> serials,Long quantity,Long itemMasterId){
		this.serials = serials;
		this.quantity = quantity;
		this.itemMasterId = itemMasterId;
	}
	
	
	public String getSerialNumbers() {
		return serialNumbers;
	}

	public void setSerialNumbers(String serialNumbers) {
		this.serialNumbers = serialNumbers;
	}

	public InventoryItemSerialNumberData() {
		
	}
	
	public InventoryItemSerialNumberData(final String serialNumbers){
		this.serialNumbers = serialNumbers;
	}	
}
