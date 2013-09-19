package org.mifosplatform.billing.servicemaster.data;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;


public class ServiceMasterOptionsData {
private Collection<ServiceMasterData> serviceMasterOptions;
private Long id;
private String serviceCode;
private String serviceDescription;
private String serviceType;
private String serviceStatus;
private String serviceUnitType;
private String isOptional;
private List<EnumOptionData> serviceTypes,serviceUnitTypes,status;

public ServiceMasterOptionsData(Collection<ServiceMasterData> serviceMasterOptions)
{
	this.serviceMasterOptions=serviceMasterOptions;
}

public ServiceMasterOptionsData(Long id, String serviceCode,
		String serviceDescription,String serviceType, String serviceUnitType, String status, String isOptional) {
	this.id=id;
	this.serviceDescription=serviceDescription;
	this.serviceCode=serviceCode;
	this.serviceType=serviceType;
	this.serviceUnitType=serviceUnitType;
	this.serviceStatus=status;
	this.isOptional=isOptional;

}


public ServiceMasterOptionsData(List<EnumOptionData> serviceType,
		List<EnumOptionData> serviceUnitType, List<EnumOptionData> status) {
	this.serviceTypes=serviceType;
	this.serviceUnitTypes=serviceUnitType;
	this.status=status;
}

public Collection<ServiceMasterData> getServiceMasterOptions() {
	return serviceMasterOptions;
}

public Long getId() {
	return id;
}

public String getServiceCode() {
	return serviceCode;
}

public String getserviceDescription() {
	return serviceDescription;
}

public String getServiceType() {
	return serviceType;
}

public void setServiceMasterOptions(
		Collection<ServiceMasterData> serviceMasterOptions) {
	this.serviceMasterOptions = serviceMasterOptions;
}

public void setId(Long id) {
	this.id = id;
}

public void setServiceCode(String serviceCode) {
	this.serviceCode = serviceCode;
}

public void setServiceDescription(String serviceDescription) {
	this.serviceDescription = serviceDescription;
}

public void setServiceType(String serviceType) {
	this.serviceType = serviceType;
}

public void setServiceStatus(String serviceStatus) {
	this.serviceStatus = serviceStatus;
}

public void setServiceUnitType(String serviceUnitType) {
	this.serviceUnitType = serviceUnitType;
}

public void setIsOptional(String isOptional) {
	this.isOptional = isOptional;
}

public void setServiceTypes(List<EnumOptionData> serviceTypes) {
	this.serviceTypes = serviceTypes;
}

public void setServiceUnitTypes(List<EnumOptionData> serviceUnitTypes) {
	this.serviceUnitTypes = serviceUnitTypes;
}

public void setStatus(List<EnumOptionData> status) {
	this.status = status;
}

public String getServiceDescription() {
	return serviceDescription;
}

public String getServiceStatus() {
	return serviceStatus;
}

public String getServiceUnitType() {
	return serviceUnitType;
}

public String getIsOptional() {
	return isOptional;
}

public List<EnumOptionData> getServiceTypes() {
	return serviceTypes;
}

public List<EnumOptionData> getServiceUnitTypes() {
	return serviceUnitTypes;
}

public List<EnumOptionData> getStatus() {
	return status;
}



}
