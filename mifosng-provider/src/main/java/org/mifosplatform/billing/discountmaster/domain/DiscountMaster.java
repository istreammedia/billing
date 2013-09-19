package org.mifosplatform.billing.discountmaster.domain;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_discount_master",uniqueConstraints = @UniqueConstraint(name = "discountcode", columnNames = { "discount_code" }))
public class DiscountMaster extends AbstractPersistable<Long> {

	
	@Column(name = "discount_code")
	private String discountCode;

	
	@Column(name = "discount_description")
	private String discountDescription;

	@Column(name = "discount_type")
	private String discountType;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "discount_rate")
	private BigDecimal discountRate;

	@Column(name = "discount_status")
	private String discountStatus;
	
	@Column(name = "is_delete")
	private char isDelete='N';


	public DiscountMaster() {
		// TODO Auto-generated constructor stub
			
	}

	public DiscountMaster(String discountCode, String discountDescription,String discountType,
			BigDecimal discountRate,LocalDate startDate, String status) {
          
		   this.discountCode=discountCode;
		   this.discountDescription=discountDescription;
		   this.discountType=discountType;
		   this.discountRate=discountRate;
		   this.startDate=startDate.toDate();
		   this.discountStatus=status;
		
	
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public String getDiscountDescription() {
		return discountDescription;
	}

	public String getDiscountType() {
		return discountType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public String getDiscountStatus() {
		return discountStatus;
	}

	public static DiscountMaster fromJson(JsonCommand command) {
		 final String discountCode = command.stringValueOfParameterNamed("discountCode");
		    final String discountDescription = command.stringValueOfParameterNamed("discountDescription");
		    final LocalDate startDate = command.localDateValueOfParameterNamed("startDate");
		    final String discountType = command.stringValueOfParameterNamed("discountType");
		    final BigDecimal discountRate= command.bigDecimalValueOfParameterNamed("discountRate");
		    final String status= command.stringValueOfParameterNamed("status");
		    
		return new DiscountMaster(discountCode,discountDescription,discountType,discountRate,startDate,status);
	}

	public Map<String, Object> update(JsonCommand command) {
		 final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);
		  final String firstnameParamName = "discountCode";
	        if (command.isChangeInStringParameterNamed(firstnameParamName, this.discountCode)) {
	            final String newValue = command.stringValueOfParameterNamed(firstnameParamName);
	            actualChanges.put(firstnameParamName, newValue);
	            this.discountCode = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String descriptionParamName = "discountDescription";
	        if (command.isChangeInStringParameterNamed(descriptionParamName, this.discountDescription)) {
	            final String newValue = command.stringValueOfParameterNamed(descriptionParamName);
	            actualChanges.put(descriptionParamName, newValue);
	            this.discountDescription = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        
	        final String discountTypeParam = "discountType";
	        if (command.isChangeInStringParameterNamed(discountTypeParam, this.discountType)) {
	            final String newValue = command.stringValueOfParameterNamed(discountTypeParam);
	            actualChanges.put(discountTypeParam, newValue);
	            this.discountType = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String startDateParamName = "startDate";
			if (command.isChangeInLocalDateParameterNamed(startDateParamName,
					new LocalDate(this.startDate))) {
				final LocalDate newValue = command
						.localDateValueOfParameterNamed(startDateParamName);
				actualChanges.put(startDateParamName, newValue);
			}
			
		        final String discountRateParamName = "discountRate";
				if (command.isChangeInBigDecimalParameterNamed(discountRateParamName,this.discountRate)) {
					final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(discountRateParamName);
					actualChanges.put(discountRateParamName, newValue);
					this.discountRate=newValue;
				}
				

		        final String statusParamName = "status";
				if (command.isChangeInStringParameterNamed(statusParamName,this.discountStatus)) {
					final String newValue = command.stringValueOfParameterNamed(statusParamName);
					actualChanges.put(statusParamName, newValue);
					this.discountStatus=newValue;
				}
				
	        return actualChanges;

	}

	public void delete() {
		
		if(this.isDelete == 'N'){
			this.isDelete='Y';
			this.discountCode=this.discountCode+"_Deleted";
		}
		
	}
 


}
