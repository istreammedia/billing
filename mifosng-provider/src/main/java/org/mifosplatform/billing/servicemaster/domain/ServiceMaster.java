package org.mifosplatform.billing.servicemaster.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_service", uniqueConstraints = @UniqueConstraint(name = "service_code_key", columnNames = { "service_code" }))
public class ServiceMaster extends AbstractPersistable<Long> {

	@Column(name = "service_code", nullable = false, length = 20)
	private String serviceCode;

	@Column(name = "service_description", nullable = false, length = 100)
	private String serviceDescription;

	@Column(name = "service_type", nullable = false, length = 100)
	private String serviceType;
	
	@Column(name = "service_unittype", nullable = false, length = 100)
	private String serviceUnittype;
	
	@Column(name = "status", nullable = false, length = 100)
	private String status;
	
	@Column(name = "is_optional", nullable = false, length = 100)
	private char isOptional;

	@Column(name = "is_deleted")
	private String isDeleted="n";


public ServiceMaster()
{}


public static ServiceMaster fromJson(final JsonCommand command) {
    final String serviceCode = command.stringValueOfParameterNamed("serviceCode");
    final String serviceDescription = command.stringValueOfParameterNamed("serviceDescription");
    final String serviceType = command.stringValueOfParameterNamed("serviceType");
   // final String serviceUnitType = command.stringValueOfParameterNamed("serviceUnitType");
    final String status= command.stringValueOfParameterNamed("status");
    final boolean isOptional= command.booleanPrimitiveValueOfParameterNamed("isOptional");
    
    return new ServiceMaster(serviceCode,serviceDescription,serviceType,status,isOptional);
}

	public ServiceMaster(String serviceCode, String serviceDescription,
			String serviceType,String  status,boolean isOptional) {
		this.serviceCode = serviceCode;
		this.serviceDescription = serviceDescription;
		this.serviceType = serviceType;
	//	this.serviceUnittype=serviceUnitType;
		this.status=status;
		this.isOptional=isOptional?'Y':'N';
	}

	

	public String getServiceCode() {
		return this.serviceCode;
	}

	public String getServiceDescription() {
		return this.serviceDescription;
	}

	public String getServiceType() {
		return this.serviceType;
	}


	public String getIsDeleted() {
		return this.isDeleted;
	}


	public  Map<String, Object> update(JsonCommand command) {
		
		  final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);
		  final String serviceCodeParamName = "serviceCode";
	        if (command.isChangeInStringParameterNamed(serviceCodeParamName, this.serviceCode)) {
	            final String newValue = command.stringValueOfParameterNamed(serviceCodeParamName);
	            actualChanges.put(serviceCodeParamName, newValue);
	            this.serviceCode = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String servicedescParamName = "serviceDescription";
	        if (command.isChangeInStringParameterNamed(servicedescParamName, this.serviceDescription)) {
	            final String newValue = command.stringValueOfParameterNamed(servicedescParamName);
	            actualChanges.put(servicedescParamName, newValue);
	            this.serviceDescription = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        final String serviceTypeParamName = "serviceType";
			if (command.isChangeInStringParameterNamed(serviceTypeParamName,
					this.serviceType)) {
				final String newValue = command.stringValueOfParameterNamed(serviceTypeParamName);
				actualChanges.put(serviceTypeParamName, newValue);
				this.serviceType=StringUtils.defaultIfEmpty(newValue,null);
			}
			
			final String serviceUnitTypeParamName = "serviceUnitType";
	        if (command.isChangeInStringParameterNamed(serviceUnitTypeParamName, this.serviceUnittype)) {
	            final String newValue = command.stringValueOfParameterNamed(serviceUnitTypeParamName);
	            actualChanges.put(serviceUnitTypeParamName, newValue);
	            this.serviceUnittype = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String statusParamName = "status";
	        if (command.isChangeInStringParameterNamed(statusParamName, this.status)) {
	            final String newValue = command.stringValueOfParameterNamed(statusParamName);
	            actualChanges.put(statusParamName, newValue);
	            this.status = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	       
	        final boolean isOptional= command.booleanPrimitiveValueOfParameterNamed("isOptional");
	        this.isOptional=isOptional?'Y':'N';
	        return actualChanges;

	}

	public void delete() {
		if(isDeleted.equalsIgnoreCase("y"))
		{}
		else
		{
			this.serviceCode=this.serviceCode+"_"+this.getId();
			isDeleted="y";
		}
	}



}
