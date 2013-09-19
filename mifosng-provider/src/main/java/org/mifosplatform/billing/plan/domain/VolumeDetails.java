package org.mifosplatform.billing.plan.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.api.JsonCommand;

@Entity
@Table(name = "b_volume_details")
public class VolumeDetails {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	
	@Column(name = "plan_id")
	private Long planId;
	
	@Column(name = "volume_type")
	private String volumeType;

	@Column(name = "units")
	private Long units;

	
	@Column(name = "units_type")
	private String unitsType;
	
	
	public VolumeDetails(){
		
	}
	
	public   VolumeDetails(Long id,Long planId, String volumeType, Long units, String unitType) {
		this.id=id;
		this.planId=planId;
		this.volumeType=volumeType;
		this.units=units;
		this.unitsType=unitType;
		
	
	}


	public static VolumeDetails fromJson(JsonCommand command, Plan plan) {
		    String volumeType=command.stringValueOfParameterNamed("volume");
		    Long units=command.longValueOfParameterNamed("units");
		    String unitType=command.stringValueOfParameterNamed("unitType");
		    Long planId=plan.getId();
			return new VolumeDetails(null,planId,volumeType,units,unitType);
	}


	public Map<String, Object> update(JsonCommand command, Long planId) {
		 final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);
		 
			this.planId=planId;
		  final String firstnameParamName = "volume";
	        if (command.isChangeInStringParameterNamed(firstnameParamName, this.volumeType)) {
	            final String newValue = command.stringValueOfParameterNamed(firstnameParamName);
	            actualChanges.put(firstnameParamName, newValue);
	            this.volumeType = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String descriptionParamName = "unitType";
	        if (command.isChangeInStringParameterNamed(descriptionParamName, this.unitsType)) {
	            final String newValue = command.stringValueOfParameterNamed(descriptionParamName);
	            actualChanges.put(firstnameParamName, newValue);
	            this.unitsType = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String unitsParamName = "units";
			if (command.isChangeInLongParameterNamed(unitsParamName,this.units)) {
				final Long newValue = command
						.longValueOfParameterNamed(unitsParamName);
				actualChanges.put(unitsParamName, newValue);
				this.units=newValue;
				
			}
			return actualChanges;
	        
		
	}
}