package org.mifosplatform.billing.adjustment.data;

import java.math.BigDecimal;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.codes.data.CodeData;

public class AdjustmentData {
	
	


	@SuppressWarnings("unused")
	Long id;
	@SuppressWarnings("unused")
	Long client_id;
	@SuppressWarnings("unused")
	LocalDate adjustment_date;
	@SuppressWarnings("unused")
	String adjustment_code;
	@SuppressWarnings("unused")
	String adjustment_type;
	@SuppressWarnings("unused")
	BigDecimal amount_paid;
	@SuppressWarnings("unused")
	Long bill_id;
	@SuppressWarnings("unused")
	Long external_id;
	@SuppressWarnings("unused")
	String Remarks;
	
	public AdjustmentData(Long id, Long client_id,
			LocalDate adjustment_date, String adjustment_code,
			BigDecimal amount_paid, Long bill_id, Long external_id,String Remarks) {
		this.id = id;
		this.client_id = client_id;
		this.adjustment_date = adjustment_date;
		this.adjustment_code = adjustment_code;
		this.amount_paid = amount_paid;
		this.bill_id = bill_id;
		this.external_id = external_id;
		this.Remarks = Remarks;
	}

	public AdjustmentData(Long id, String adjustment_code) {

		this.id=id;
		this.adjustment_code=adjustment_code;
		
	}
	
	
	public static AdjustmentData instance(Long id,Long client_id,LocalDate adjustment_date,String adjustment_code,
			BigDecimal amount_paid,Long bill_id,Long external_id,String Remarks){
		
		return new AdjustmentData(id,client_id,adjustment_date,adjustment_code,amount_paid,bill_id,external_id,Remarks);
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAdjustment_code() {
		return adjustment_code;
	}

	public void setAdjustment_code(String adjustment_code) {
		this.adjustment_code = adjustment_code;
	}
	
	

}
