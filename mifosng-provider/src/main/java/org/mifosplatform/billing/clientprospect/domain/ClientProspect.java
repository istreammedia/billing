package org.mifosplatform.billing.clientprospect.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.portfolio.client.api.ClientApiConstants;
import org.mifosplatform.portfolio.client.domain.ClientEnumerations;
import org.mifosplatform.portfolio.client.domain.ClientStatus;
import org.mifosplatform.useradministration.domain.AppUser;

import com.google.gson.JsonElement;


@Entity
@Table(name="b_prospect")
public class ClientProspect extends AbstractAuditableCustom<AppUser, Long> {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="prospect_type", length=1 )
	private Short prospectType;
	
	@Column(name="first_name", length=50 )
	private String firstName;
	
	@Column(name="middle_name", length=50 )
	private String middleName;
	
	@Column(name="last_name", length=50)
	private String lastName;
	
	@Column(name="home_phone_number", length=20 )
	private String homePhoneNumber;
	
	@Column(name="work_phone_number", length=20 )
	private String workPhoneNumber;
	
	@Column(name="mobile_number", length=20 )
	private String mobileNumber;
	
	@Column(name="email", length=50 )
	private String email;
	
	
	@Column(name="source_of_publicity", length=50 )
	private String sourceOfPublicity;
	
	@Column(name="preferred_plan")
	private String preferredPlan;
	
	@Column(name="preferred_calling_time")
	private Date preferredCallingTime;
	
	@Column(name="note", length=100 )
	private String note;
	
	@Column(name="address", length=100 )
	private String address;
	
	@Column(name="street_area", length=100 )
	private String streetArea;
	
	@Column(name="city_district", length=100 )
	private String cityDistrict;
	
	@Column(name="state", length=100)
	private String state;
	
	@Column(name="country", length=100)
	private String country;

	@Column(name="status", length=100)
	private String status="New";
	
	@Column(name="status_remark", length=50)
	private String statusRemark;
	
	@Column(name="zip_code")
	private String zipCode;
	
	@Column(name="is_deleted")
	private char isDeleted='N';
	
	public ClientProspect() {
		
	}
	
	public ClientProspect(final Short prospectType, final String firstName, final String middleName, final String lastName, final String homePhoneNumber, final String workPhoneNumber, final String mobileNumber, final String email, final String sourceOfPublicity, final Date preferredCallingTime, final String note, final String address, final String streetArea, final String cityDistrict, final String state, final String country,final String zipCode){
		this.prospectType = prospectType;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.homePhoneNumber = homePhoneNumber;
		this.workPhoneNumber = workPhoneNumber;
		this.mobileNumber = mobileNumber;
		this.email = email;
		this.sourceOfPublicity = sourceOfPublicity;
		this.preferredCallingTime = preferredCallingTime;
		this.note = note;
		this.address = address;
		this.streetArea = streetArea;
		this.cityDistrict = cityDistrict;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
	}
	
	public ClientProspect(final Short prospectType, final String firstName, final String middleName, final String lastName, final String homePhoneNumber, final String workPhoneNumber, final String mobileNumber, final String email, final String sourceOfPublicity, final Date preferredCallingTime, final String note, final String address, final String streetArea, final String cityDistrict, final String state, final String country, final String preferredPlan, final String status, final String statusRemark, final String zipCode){
		this.prospectType = prospectType;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.homePhoneNumber = homePhoneNumber;
		this.workPhoneNumber = workPhoneNumber;
		this.mobileNumber = mobileNumber;
		this.email = email;
		this.sourceOfPublicity = sourceOfPublicity;
		this.preferredPlan = preferredPlan;
		this.preferredCallingTime = preferredCallingTime;
		this.note = note;
		this.address = address;
		this.streetArea = streetArea;
		this.cityDistrict = cityDistrict;
		this.state = state;
		this.country = country;
		this.status = status;
		this.statusRemark = statusRemark;
		this.zipCode = zipCode;
	}

	public Short getProspectType() {
		return prospectType;
	}

	public void setProspectType(Short prospectType) {
		this.prospectType = prospectType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	public String getWorkPhoneNumber() {
		return workPhoneNumber;
	}

	public void setWorkPhoneNumber(String workPhoneNumber) {
		this.workPhoneNumber = workPhoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSourceOfPublicity() {
		return sourceOfPublicity;
	}

	public void setSourceOfPublicity(String sourceOfPublicity) {
		this.sourceOfPublicity = sourceOfPublicity;
	}

	public Date getPreferredCallingTime() {
		return preferredCallingTime;
	}

	public void setPreferredCallingTime(Date preferredCallingTime) {
		this.preferredCallingTime = preferredCallingTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStreetArea() {
		return streetArea;
	}

	public void setStreetArea(String streetArea) {
		this.streetArea = streetArea;
	}

	public String getCityDistrict() {
		return cityDistrict;
	}

	public void setCityDistrict(String cityDistrict) {
		this.cityDistrict = cityDistrict;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getPreferredPlan() {
		return preferredPlan;
	}

	public void setPreferredPlan(String preferredPlan) {
		this.preferredPlan = preferredPlan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	public static ClientProspect fromJson(final FromJsonHelper fromJsonHelper, final JsonCommand command) throws ParseException{
			
		final JsonElement element = fromJsonHelper.parse(command.json());
		
		Short prospectType = command.integerValueOfParameterNamed("prospectType").shortValue();		
		
		ClientProspect clientProspect = new ClientProspect();
		clientProspect.setProspectType(prospectType);
		
		if(fromJsonHelper.parameterExists("firstName", element)){
			String firstName = command.stringValueOfParameterNamed("firstName");
			clientProspect.setFirstName(firstName);
		}
		
		if(fromJsonHelper.parameterExists("middleName", element)){
			String middleName = command.stringValueOfParameterNamed("middleName");
			clientProspect.setMiddleName(middleName);
		}
		
		if(fromJsonHelper.parameterExists("lastName", element)){
			String lastName = command.stringValueOfParameterNamed("lastName");
			clientProspect.setLastName(lastName);
		}
		
		if(fromJsonHelper.parameterExists("homePhoneNumber", element)){
			String homePhoneNumber = command.stringValueOfParameterNamed("homePhoneNumber");
			clientProspect.setHomePhoneNumber(homePhoneNumber);
		}
		
		if(fromJsonHelper.parameterExists("workPhoneNumber", element)){
			String workPhoneNumber = command.stringValueOfParameterNamed("workPhoneNumber");
			clientProspect.setWorkPhoneNumber(workPhoneNumber);
		}
		
		if(fromJsonHelper.parameterExists("mobileNumber", element)){
			String mobileNumber = command.stringValueOfParameterNamed("mobileNumber");
			clientProspect.setMobileNumber(mobileNumber);
		}
		
		if(fromJsonHelper.parameterExists("email", element)){
			String email = command.stringValueOfParameterNamed("email");
			clientProspect.setEmail(email);
		}
		
		if(fromJsonHelper.parameterExists("sourceOfPublicity", element)){
			String sourceOfPublicity = command.stringValueOfParameterNamed("sourceOfPublicity");
			String sourceOther = command.stringValueOfParameterNamed("sourceOther");
			if(!(sourceOther == "" || sourceOther == null || sourceOther.equals(""))){
				sourceOfPublicity = sourceOther;
			}
			
			clientProspect.setSourceOfPublicity(sourceOfPublicity);
		}
		
		
		
		if(fromJsonHelper.parameterExists("preferredCallingTime", element)){
			String startDateString = command.stringValueOfParameterNamed("preferredCallingTime");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date preferredCallingTime = df.parse(startDateString);
			clientProspect.setPreferredCallingTime(preferredCallingTime);
		}
		
		if(fromJsonHelper.parameterExists("note", element)){
			String note = command.stringValueOfParameterNamed("note");
			clientProspect.setNote(note);
		}
		
		if(fromJsonHelper.parameterExists("address", element)){
			String address = command.stringValueOfParameterNamed("address");
			clientProspect.setAddress(address);
		}
		
		if(fromJsonHelper.parameterExists("streetArea", element)){
			String streetArea = command.stringValueOfParameterNamed("streetArea");
			clientProspect.setStreetArea(streetArea);
		}
		
		if(fromJsonHelper.parameterExists("cityDistrict", element)){
			String cityDistrict = command.stringValueOfParameterNamed("cityDistrict");
			clientProspect.setCityDistrict(cityDistrict);
		}
		
		if(fromJsonHelper.parameterExists("state", element)){
			String state = command.stringValueOfParameterNamed("state");
			clientProspect.setState(state);
		}
		
		if(fromJsonHelper.parameterExists("country", element)){
			String country = command.stringValueOfParameterNamed("country");
			clientProspect.setCountry(country);
		}
		
		if(fromJsonHelper.parameterExists("preferredPlan", element)){
			String preferredPlan = command.stringValueOfParameterNamed("preferredPlan");
			clientProspect.setPreferredPlan(preferredPlan);
		}
		
		if(fromJsonHelper.parameterExists("status", element)){
			String status = command.stringValueOfParameterNamed("status");
			clientProspect.setStatus(status);
		}
		
		if(fromJsonHelper.parameterExists("zipCode", element))
		{
			String zipCode = command.stringValueOfParameterNamed("zipCode");
			clientProspect.setZipCode(zipCode);
		}
		
		return clientProspect;
	}

	public static Map<String,Object> update(final JsonCommand command){
		Map<String,Object> actualChanges = new LinkedHashMap<String, Object>(1);
		 
		return null;
	}

	public String getStatusRemark() {
		return statusRemark;
	}

	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	}

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
}
