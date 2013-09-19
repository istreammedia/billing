package org.mifosplatform.billing.address.data;

import java.util.List;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class AddressData {
	
	private Long id;
	private Long clientId;
	private String addressKey;
	private String addressNo;
	private String street;
	private String zip;
	private String city;
	private String state;
	private String country;
	private List<AddressData> datas;
	private List<String> countryData,stateData,cityData;
	private String data;
	private List<EnumOptionData> addressOptionsData; 
	private List<AddressData> addressData;
	private String addressType;
	
	

	public AddressData(Long addressId, Long clientId, String addressKeyId,
			String addressNo, String street, String zip, String city,
			String state, String country, String addressKey) {
     
		this.id=addressId;
		this.addressKey=addressKeyId;
		this.clientId=clientId;
		this.addressNo=addressNo;
		this.street=street;
		this.zip=zip;
		this.city=city;
		this.state=state;
		this.country=country;
		this.addressType=addressKey;
	
	
	}



	public AddressData(List<AddressData> addressdata, List<String> countryData, List<String> statesData, List<String> citiesData, List<EnumOptionData> enumOptionDatas) {
		if(addressdata!=null && addressdata.size()!=0){
		this.id=addressdata.get(0).getAddressId();
		this.addressKey=addressdata.get(0).getAddressKey();
		this.clientId=addressdata.get(0).getClientId();
		this.addressNo=addressdata.get(0).getAddressNo();
		this.street=addressdata.get(0).getStreet();
		this.zip=addressdata.get(0).getZip();
		this.city=addressdata.get(0).getCity();
		this.state=addressdata.get(0).getState();
		this.country=addressdata.get(0).getCountry();
		
		}
	this.datas=addressdata;
	this.countryData=countryData;
	this.stateData=statesData;
	this.cityData=citiesData;
	this.addressOptionsData=enumOptionDatas;
	}



	public AddressData(Long id, String data) {

	this.id=id;
	this.data=data;
	
	}

   public AddressData(String city,String state, String country) {
		// TODO Auto-generated constructor stub
	   this.city=city;
	   this.state=state;
	   this.country=country;
	}



   public AddressData(List<AddressData> data) {
	// TODO Auto-generated constructor stub
	this.addressData=data;
   }


	public Long getAddressId() {
		return id;
	}



	public Long getClientId() {
		return clientId;
	}



	public String getAddressKey() {
		return addressKey;
	}



	public String getAddressNo() {
		return addressNo;
	}



	public Long getId() {
		return id;
	}



	public List<AddressData> getDatas() {
		return datas;
	}



	public List<String> getCountryData() {
		return countryData;
	}



	public List<String> getStateData() {
		return stateData;
	}



	public List<String> getCityData() {
		return cityData;
	}



	public String getData() {
		return data;
	}



	public List<EnumOptionData> getAddressOptionsData() {
		return addressOptionsData;
	}



	public List<AddressData> getAddressData() {
		return addressData;
	}



	public String getAddressType() {
		return addressType;
	}



	public String getStreet() {
		return street;
	}



	public String getZip() {
		return zip;
	}



	public String getCity() {
		return city;
	}



	public String getState() {
		return state;
	}



	public String getCountry() {
		return country;
	}
	
	

}
