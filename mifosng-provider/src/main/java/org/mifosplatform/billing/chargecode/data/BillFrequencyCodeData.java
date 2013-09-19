package org.mifosplatform.billing.chargecode.data;

public class BillFrequencyCodeData {

	private String billFrequencyCode;
	private Long id;
	
	
	public BillFrequencyCodeData(){}
	
	public BillFrequencyCodeData(String billFrequencyCode,Long id){
		this.billFrequencyCode = billFrequencyCode;
		this.id = id; 
	}
	
	public void setId(final Long id){
		this.id=id;
	}
	
	public void setBillFrequencyCode(final String billFrequencyCode){
		this.billFrequencyCode = billFrequencyCode;
	}
	
	public String getBillFrequencyCode(){
		return this.billFrequencyCode;
	}
	
	public Long getId(){
		return this.id;
	} 
	
}
