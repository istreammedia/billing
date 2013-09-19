package org.mifosplatform.billing.servicemaster.data;

public class ServiceMasterData {

private String serviceType;
private String categoryType;

public ServiceMasterData(String serviceType,String categoryType)
{
	this.serviceType=serviceType;
	this.categoryType=categoryType;
}

public String getServiceType() {
	return serviceType;
}

public void setServiceType(String serviceType) {
	this.serviceType = serviceType;
}

public String getCategoryType() {
	return categoryType;
}

public void setCategoryType(String categoryType) {
	this.categoryType = categoryType;
}

}
