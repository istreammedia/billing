package org.mifosplatform.billing.inventory.exception;


@SuppressWarnings("serial")
public class InventoryItemDetailsExist extends AbstractInventoryItemDetailsExist{

	public InventoryItemDetailsExist(final String status,final String errorMessage,final String resourceIdentifier,final String parameterName){
		super(status,errorMessage,resourceIdentifier,parameterName);
	}
	
}