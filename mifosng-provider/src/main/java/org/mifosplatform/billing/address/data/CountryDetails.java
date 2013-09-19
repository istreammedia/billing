package org.mifosplatform.billing.address.data;

public class CountryDetails {

	private final Long id;
	private final String countryName;
	
	public CountryDetails(Long id, String countryName) {
           
		this.id=id;
		this.countryName=countryName;
	
	
	}

	public Long getId() {
		return id;
	}

	public String getCountryName() {
		return countryName;
	}
	
	

}
