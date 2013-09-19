package org.mifosplatform.billing.randomgenerator.data;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class RandomGeneratorData {
	
	 Long id;
	 String batchName;
	 String batchDescription;
	 Long length;
	 String pinCategory;
	 String pinType;
	 Long quantity;
	 String serialNo;
	 LocalDate expiryDate;
	 String beginWith;
	 String pinValue;
	 private List<EnumOptionData> pinCategoryData;
	 private List<EnumOptionData> pinTypeData;
	
	public RandomGeneratorData(String batchName, String batchDescription,
			Long length, String pinCategory, String pinType, Long quantity,
			String serial, Date expiryDate, String beginWith,
			String pinValue, Long id) {
		// TODO Auto-generated constructor stub
		this.batchName=batchName;
		this.batchDescription=batchDescription;
		this.length=length;
		this.pinCategory=pinCategory;
		this.pinType=pinType;
		this.quantity=quantity;
		this.beginWith=beginWith;
		this.serialNo=serial;
		this.expiryDate=new LocalDate(expiryDate);
		this.pinValue=pinValue;
		this.id=id;
	}

	public RandomGeneratorData() {
		// TODO Auto-generated constructor stub
	}

	public RandomGeneratorData(List<EnumOptionData> pinCategoryData,
			List<EnumOptionData> pinTypeData) {
		
		this.pinCategoryData=pinCategoryData;
		this.pinTypeData=pinTypeData;
	}

	public List<EnumOptionData> getPinCategoryData() {
		return pinCategoryData;
	}

	public void setPinCategoryData(List<EnumOptionData> pinCategoryData) {
		this.pinCategoryData = pinCategoryData;
	}

	public List<EnumOptionData> getPinTypeData() {
		return pinTypeData;
	}

	public void setPinTypeData(List<EnumOptionData> pinTypeData) {
		this.pinTypeData = pinTypeData;
	}

	public String getBatchName() {
		return batchName;
	}
	

	public Long getId() {
		return id;
	}

	public String getBeginWith() {
		return beginWith;
	}
    
	public String getPinValue() {
		return pinValue;
	}

	public void setPinValue(String pinValue) {
		this.pinValue = pinValue;
	}

	public void setBeginWith(String beginWith) {
		this.beginWith = beginWith;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getBatchDescription() {
		return batchDescription;
	}

	public void setBatchDescription(String batchDescription) {
		this.batchDescription = batchDescription;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public String getPinCategory() {
		return pinCategory;
	}

	public void setPinCategory(String pinCategory) {
		this.pinCategory = pinCategory;
	}

	public String getPinType() {
		return pinType;
	}

	public void setPinType(String pinType) {
		this.pinType = pinType;
	}

	public Long getQuantity() {
		return quantity;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	
	

}
