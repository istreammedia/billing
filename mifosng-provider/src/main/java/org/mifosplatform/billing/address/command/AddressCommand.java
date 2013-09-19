package org.mifosplatform.billing.address.command;

import java.util.Set;

import org.joda.time.LocalDate;

public class AddressCommand {
	
	private Long clientId;
	private String addressKey;
	private String addressNo;
	private String street;
	private String city;
	private String state;
	private String country;
	private String zip;
	private Set<String> modifiedParameters;
	

	public AddressCommand(Set<String> modifiedParameters, Long clientid, String addressKey, String addressNo,
			String street, String zip, String city, String state,
			String country) {
		
		this.modifiedParameters=modifiedParameters;		
		this.clientId=clientid;
		this.addressKey=addressKey;
		this.addressNo=addressNo;
		this.state=state;
		this.street=street;
		this.city=city;
		this.country=country;
		this.zip=zip;
		
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



	public String getStreet() {
		return street;
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
	


	public String getZip() {
		return zip;
	}
	public boolean isAddressKeyChanged() {
		return this.modifiedParameters.contains("addressKey");
	}

	public boolean isAddressNOChanged() {
		return this.modifiedParameters.contains("addressNo");
	}

	public boolean isStreetChanged() {
		return this.modifiedParameters.contains("street");
	}
	
	public boolean isCityChanged() {
		return this.modifiedParameters.contains("city");
	}

	public boolean isStateChanged() {
		return this.modifiedParameters.contains("state");
	}

	public boolean isCountryChanged() {
		return this.modifiedParameters.contains("country");
	}
	public boolean isZipChanged() {
		return this.modifiedParameters.contains("zip");
	}

	

}
