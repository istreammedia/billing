package org.mifosplatform.billing.inventory.data;

public class QuantityData {

	private Long quantity;
	
	public QuantityData(){}
	
	public QuantityData(Long quantity){
		this.quantity = quantity;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
}
