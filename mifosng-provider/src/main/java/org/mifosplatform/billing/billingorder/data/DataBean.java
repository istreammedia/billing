package org.mifosplatform.billing.billingorder.data;
public class DataBean {
private String clientName;
private String addrNo;
private String street;
private String city;
private String state;
private String country;
private String zipcode;

public DataBean(String clientName, String addrNo, String street,
		String city, String state, String country, String zipcode) {
	
	this.clientName=clientName;
	this.city=city;
	this.addrNo=addrNo;
	this.street=street;
	this.state=state;
	this.country=country;
	this.zipcode=zipcode;
	
}

public String getClientName() {
	return clientName;
}

public String getAddrNo() {
	return addrNo;
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

public String getZipcode() {
	return zipcode;
}


}
