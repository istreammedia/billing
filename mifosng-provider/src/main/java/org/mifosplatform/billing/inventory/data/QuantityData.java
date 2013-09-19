package org.mifosplatform.billing.inventory.data;

public class QuantityData {

	private Long quantity;
    private String serailnumber;
	
	public QuantityData(){}
	
	public QuantityData(Long quantity){
		this.quantity = quantity;
	}
	
	public QuantityData(String serialNumber) {
		this.serailnumber=serialNumber;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
}
