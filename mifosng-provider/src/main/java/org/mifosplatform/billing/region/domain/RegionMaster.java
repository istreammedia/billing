package org.mifosplatform.billing.region.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_priceregion_master", uniqueConstraints = @UniqueConstraint(name = "priceregion_code_key", columnNames = { "priceregion_code" }))
public class RegionMaster extends AbstractPersistable<Long> {

	@Column(name = "priceregion_code", nullable = false, length = 20)
	private String regionCode;

	@Column(name = "priceregion_name", nullable = false, length = 100)
	private String regionName;

	@Column(name = "createdby_id", nullable = false, length = 100)
	private Long createdbyId;
	
	@Column(name = "created_date", nullable = false, length = 100)
	private Date createdDate;

	@Column(name = "is_deleted")
	private char isDeleted='N';
	
	

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "regionMaster", orphanRemoval = true)
	private List<RegionDetails> details = new ArrayList<RegionDetails>();


public RegionMaster()
{}

	public RegionMaster(String regionCode, String regionName) {

	  this.regionCode=regionCode;
	  this.regionName=regionName;
	  this.createdDate=new Date();
	
	}







	public char getIsDeleted() {
		return this.isDeleted;
	}


	public  Map<String, Object> update(JsonCommand command) {
		
		  final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);
		  final String regionCodeParamName = "regionCode";
	        if (command.isChangeInStringParameterNamed(regionCodeParamName, this.regionCode)) {
	            final String newValue = command.stringValueOfParameterNamed(regionCodeParamName);
	            actualChanges.put(regionCodeParamName, newValue);
	            this.regionCode = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String regionNameParamName = "regionName";
	        if (command.isChangeInStringParameterNamed(regionNameParamName, this.regionName)) {
	            final String newValue = command.stringValueOfParameterNamed(regionNameParamName);
	            actualChanges.put(regionNameParamName, newValue);
	            this.regionName = StringUtils.defaultIfEmpty(newValue, null);
	        }
			return actualChanges;
	       
	        
	       
	       
	       

	}

	public void delete() {
		
		if(isDeleted == 'N'){
			this.isDeleted='Y';
			this.regionCode=this.getId()+"_"+this.regionCode+"_DELETED";
			
			for(RegionDetails detail:this.details){
				detail.delete();
			}
					
		}
		
		
		
	}

	public String getRegionCode() {
		return regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public Long getCreatedbyId() {
		return createdbyId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public List<RegionDetails> getDetails() {
		return details;
	}

	public static RegionMaster fromJson(JsonCommand command) {
		final String regionCode = command.stringValueOfParameterNamed("regionCode");
	    final String regionName = command.stringValueOfParameterNamed("regionName");
	    
	return new RegionMaster(regionCode,regionName);
	}

	public void addRegionDetails(RegionDetails detail) {
		detail.update(this);
		this.details.add(detail);
		
	}



}
