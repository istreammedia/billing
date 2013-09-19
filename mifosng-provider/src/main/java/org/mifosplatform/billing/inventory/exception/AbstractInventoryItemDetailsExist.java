package org.mifosplatform.billing.inventory.exception;

public class AbstractInventoryItemDetailsExist extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status = null;
	private String errorMessage = null;
	private String resourceIdentifier = null;
	private String parameterName = null; 
	
	

	public AbstractInventoryItemDetailsExist(String status,String errorMessage,String resourceIdentifier,String parameterName){
		this.status = status;
		this.errorMessage = errorMessage;
		this.resourceIdentifier = resourceIdentifier;
		this.parameterName = parameterName;
	}
	
	public String getParameterName(){
		return parameterName;
	}
	
	public void setParameterName(String parameterName){
		this.parameterName = parameterName;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getResourceIdentifier() {
		return resourceIdentifier;
	}

	public void setResourceIdentifier(String resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
	}
	
	
}
