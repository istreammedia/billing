package org.mifosplatform.billing.inventory.exception;


public class ApiInventoryErrorResponse {

	private String status = null;
	private String errorMessage = null;
	private String resourceIdentifier = null;
	private String statusCode  = null;
	private String parameterName = null;
	private String[] errors = null;
	
	
	public static ApiInventoryErrorResponse dataIntegrityError(final String status, final String errorMessage,
            final String resourceIdentifier, final String parameterName) {

		ApiInventoryErrorResponse globalErrorResponse = new ApiInventoryErrorResponse();
		globalErrorResponse.setStatusCode("403");
        globalErrorResponse.setStatus(status);
        globalErrorResponse.setErrorMessage(errorMessage);
        globalErrorResponse.setResourceIdentifier(resourceIdentifier);
        globalErrorResponse.setParameterName(parameterName);
        globalErrorResponse.setErrors(new String[]{"1","2"});
        return globalErrorResponse;
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



	public String getStatusCode() {
		return statusCode;
	}



	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}



	public String getParameterName() {
		return parameterName;
	}



	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}



	public String[] getErrors() {
		return errors;
	}



	public void setErrors(String[] errors) {
		this.errors = errors;
	}
	
}
