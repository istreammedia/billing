package org.mifosplatform.billing.randomgenerator.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_pin_master", uniqueConstraints = @UniqueConstraint(name = "batch_name", columnNames = { "batch_name" }))
public class RandomGenerator extends  AbstractPersistable<Long>  {
	
	
	@Column(name = "batch_name", nullable = false)
	private String batchName;

	@Column(name = "batch_description", nullable = false)
	private String batchDescription;

	@Column(name = "length", nullable = false)
	private Long length;

	@Column(name = "begin_with")
	private String beginWith;

	@Column(name = "pin_category", nullable = false)
	private String pinCategory;

	@Column(name = "quantity", nullable = false)
	private Long quantity;
	
	@Column(name = "serial_no")
	private String serialNo;

	@Column(name = "pin_type")
	private String pinType;
	
	@Column(name = "pin_value")
	private String pinValue;

	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "randomGenerator", orphanRemoval = true)
	private List<RandomGeneratorDetails> randomGeneratorDetails = new ArrayList<RandomGeneratorDetails>();
	
	


public RandomGenerator(){
	
}


	public RandomGenerator(String batchName, String batchDescription,
		Long length, String beginWith, String pinCategory, Long quantity,
		String serialNo, String pinType, String pinValue, Date date) {	
	
		this.batchName=batchName;
		this.batchDescription=batchDescription;
		this.length=length;
		this.beginWith=beginWith;
		this.pinCategory=pinCategory;
		this.quantity=quantity;
		this.serialNo=serialNo;
		this.pinType=pinType;
		this.pinValue=pinValue;
		this.expiryDate=date;
		
}


	public static RandomGenerator fromJson(JsonCommand command) throws ParseException {
		    final String batchName = command.stringValueOfParameterNamed("batchName");
		    final String batchDescription = command.stringValueOfParameterNamed("batchDescription");
		    final Long length = command.longValueOfParameterNamed("length");
		    final String beginWith = command.stringValueOfParameterNamed("beginWith");
		    final String pinCategory = command.stringValueOfParameterNamed("pinCategory");
		    final Long quantity = command.longValueOfParameterNamed("quantity");
		    final String serialNo = command.stringValueOfParameterNamed("serialNo");
		    final String pinType = command.stringValueOfParameterNamed("pinType");
		    final String pinVal = command.stringValueOfParameterNamed("pinValue");
		    final String pinExtention = command.stringValueOfParameterNamed("pinExtention");
		    final LocalDate expiryDate = command.localDateValueOfParameterNamed("expiryDate");
		    String pinValue=pinVal+" "+pinExtention;
		return new RandomGenerator(batchName,batchDescription,length,beginWith,pinCategory,quantity,serialNo,pinType,pinValue,expiryDate.toDate());
	}
	
	public List<RandomGeneratorDetails> getRandomGeneratorDetails() {
		return randomGeneratorDetails;
	}


	public void add(RandomGeneratorDetails randomGeneratorDetails) {
		randomGeneratorDetails.update(this);
		this.randomGeneratorDetails.add(randomGeneratorDetails);	
	}


	public String getBatchName() {
		return batchName;
	}


	public String getBatchDescription() {
		return batchDescription;
	}


	public Long getLength() {
		return length;
	}


	public String getBeginWith() {
		return beginWith;
	}


	public String getPinCategory() {
		return pinCategory;
	}


	public Long getQuantity() {
		return quantity;
	}


	public String getSerialNo() {
		return serialNo;
	}


	public String getPinType() {
		return pinType;
	}


	public String getPinValue() {
		return pinValue;
	}


	public Date getExpiryDate() {
		return expiryDate;
	}
	
	
	
	

}
