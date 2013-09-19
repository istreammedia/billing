package org.mifosplatform.billing.paymode.data;

import java.util.Collection;

import org.joda.time.LocalDate;

public class McodeData {

	private Long id;
	private String mCodeValue;
	private LocalDate startDate;
	private Collection<McodeData> paymodeDatas;

	public static McodeData instance(Long id, String paymodeCode) {

		return new McodeData(id, paymodeCode);
	}
	

	public Long getId() {
		return id;
	}

	public String getPaymodeCode() {
		return mCodeValue;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public Collection<McodeData> getPaymodeDatas() {
		return paymodeDatas;
	}

	public McodeData(Long id, String paymodeCode) {
		this.id = id;
		this.mCodeValue = paymodeCode;

	}

	public McodeData(Collection<McodeData> data) {
		this.paymodeDatas = data;
		this.startDate = new LocalDate();
	}


	public static McodeData instance1(Long codeId) {
		
		return  new McodeData(codeId);
	}
	public McodeData(Long id){
		this.id=id;
	}
	

}
