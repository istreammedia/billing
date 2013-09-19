package org.mifosplatform.billing.inventory.data;

import java.util.ArrayList;
import java.util.List;


public class InventoryItemSerialNumberData {

	private String serialNumber;
	private List<QuantityData> serialNumbers=new ArrayList<QuantityData>();
	private Long quantity;
	private Long itemMasterId;
	private List<InventoryItemSerialNumberData> datas;
	
	public InventoryItemSerialNumberData(List<QuantityData> serials,Long quantity,Long itemMasterId){

		this.serialNumbers = serials;
		this.quantity = quantity;
		this.itemMasterId = itemMasterId;
	}
	
	
	public String getSerialNumbers() {
		return serialNumber;
	}

	public void setSerialNumbers(String serialNumbers) {
		this.serialNumber = serialNumbers;
	}

	public InventoryItemSerialNumberData() {
		
	}
	
	public InventoryItemSerialNumberData(final String serialNumbers){
		this.serialNumber = serialNumbers;
	}


	public InventoryItemSerialNumberData setSerialNumbers(List<QuantityData> itemSerialNumbers) {
		serialNumbers=itemSerialNumbers;
		return  this;
	}


	public void setListSerialNumber(List<InventoryItemSerialNumberData> datas) {
		this.datas=datas;
		
	}	
}
