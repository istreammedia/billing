package org.mifosplatform.billing.region.data;

import java.util.List;

import org.mifosplatform.billing.address.data.CountryDetails;
import org.mifosplatform.billing.address.data.StateDetails;

public class RegionData {
	
	
     private Long id;
	private  String regionCode;
	private  String regionName;
	private Long countryId;
	private List<CountryDetails> countryData;
	private List<StateDetails> statesData;
	private List<RegionDetailsData> regionDetails;
	private char isDefault;
	

	public RegionData(List<CountryDetails> countryData) {
        
		this.countryData=countryData;

		
	}

	public RegionData(List<StateDetails> statesData, String regionCode,String regionName, Long countryId,Long id) {

		 this.id=id;
         this.regionCode=regionCode;
         this.regionName=regionName;
         this.countryId=countryId;
         this.statesData=statesData;
	}

	

	public String getRegionCode() {
		return regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public List<CountryDetails> getCountryData() {
		return countryData;
	}

	public List<StateDetails> getStatesData() {
		return statesData;
	}

	public void setCountryDetails(List<CountryDetails> countrydata) {
		this.countryData=countrydata;
		
	}

	public void setStatesData(List<StateDetails> statesData) {

		this.statesData=statesData;
		
	}

	public void setRegionDetails(List<RegionDetailsData> regionDetails) {
		this.regionDetails=regionDetails;
		if(regionDetails.get(0).getStateId() == 0){
			this.isDefault='Y';
		}
		
	}

	public void setCountryId(Long countryId) {
		this.countryId=countryId;
		
	}

	

	
	
}
