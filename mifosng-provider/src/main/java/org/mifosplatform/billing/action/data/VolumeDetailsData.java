package org.mifosplatform.billing.action.data;

public class VolumeDetailsData {
	
	
	private final Long id;
	private final Long planId;
	private final String volumeType;
	private final String unitType;
	private final Long units;
	

	public VolumeDetailsData(Long id, Long planId, String volumeType,
			Long units, String unitType) {
         this.id=id;
         this.planId=planId;
         this.volumeType=volumeType;
         this.units=units;
         this.unitType=unitType;
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @return the planId
	 */
	public Long getPlanId() {
		return planId;
	}


	/**
	 * @return the volumeType
	 */
	public String getVolumeType() {
		return volumeType;
	}


	/**
	 * @return the unitType
	 */
	public String getUnitType() {
		return unitType;
	}


	/**
	 * @return the units
	 */
	public Long getUnits() {
		return units;
	}
	
	

}
