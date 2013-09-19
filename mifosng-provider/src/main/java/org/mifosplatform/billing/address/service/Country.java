package org.mifosplatform.billing.address.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.data.jpa.domain.AbstractAuditable;



@Entity
@Table(name="b_country",uniqueConstraints=@UniqueConstraint( name ="country_code",columnNames={"country_code"}))
public class Country extends AbstractAuditable<AppUser,Long>{
	

@Column(name="country_code")
private String countryCode;

@Column(name="country_name")
private String countryName;

@Column(name="is_active")
private String isActive;


public String getCountryCode() {
	return countryCode;
}


public void setCountryCode(String countryCode) {
	this.countryCode = countryCode;
}


public String getCountryName() {
	return countryName;
}


public void setCountryName(String countryName) {
	this.countryName = countryName;
}


public String getIsActive() {
	return isActive;
}


public void setIsActive(String isActive) {
	this.isActive = isActive;
}


public Country(String entityCode, String entityName) {
this.countryCode=entityCode;
this.countryName=entityName;
this.isActive="Y";


}


public static Country fromJson(JsonCommand command) {
	final String cityCode = command.stringValueOfParameterNamed("entityCode");
    final String cityName = command.stringValueOfParameterNamed("entityName");
     return new Country(cityCode,cityName);
}
}
