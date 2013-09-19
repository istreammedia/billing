package org.mifosplatform.billing.chargecode.data;

public class ChargeTypeData {

	private String chargeType;
	private Long id;
	
	
	public ChargeTypeData(){
		
	}
	
	public ChargeTypeData(final String chargeType, final Long id){
		this.chargeType = chargeType;
		this.id = id;
	}
	
	public String getChargeType(){
		return this.chargeType;
	}
	
	public void setChargeType(String chargeType){
		this.chargeType = chargeType;
	}
	
	
	public void setId(final Long id){
		this.id = id;
	}
	
	public Long getId(){
		return this.id;
	}
	
	
}
