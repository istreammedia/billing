package org.mifosplatform.billing.chargecode.data;

public class DurationTypeData {

	
	private String durationType;
	private Long id;
	
	public DurationTypeData(){}
	
	public DurationTypeData(final String durationType,final Long id){
		this.durationType = durationType;
		this.id = id;
	}
	
	
	public void setId(final Long id){
		this.id = id;
	}
	
	public void setDurationType(String durationType){
		this.durationType = durationType;
	}
	
	public String getDurationType(){
		return this.durationType;
	}
	
	public Long getId(){
		return this.id;
	}
}
