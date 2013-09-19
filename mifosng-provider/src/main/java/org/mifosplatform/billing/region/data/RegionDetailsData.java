package org.mifosplatform.billing.region.data;

public class RegionDetailsData {
	
	private final Long id;
	private final Long regionId;
	private final Long stateId;
	private final Long countryId;
	private final String stateName;
	private char isDefault;

	public RegionDetailsData(Long id, Long regionId, Long stateId,Long countryId, String stateName) {
		
		this.id=id;
		this.regionId=regionId;
		this.stateId=stateId;
		this.countryId=countryId;
		this.stateName=stateName;
		if(stateId == 0){
			this.isDefault='Y';
		}
		
	}

	public Long getId() {
		return id;
	}

	public Long getRegionId() {
		return regionId;
	}

	public Long getStateId() {
		return stateId;
	}

	public Long getCountryId() {
		return countryId;
	}
	
	

}
