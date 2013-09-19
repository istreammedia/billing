package org.mifosplatform.billing.inventory.mrn.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mifosplatform.infrastructure.core.api.JsonCommand;

public class MRNMoveDetailsData{

	private String 	serialNumber;
	private Long mrnId;
	private Date movedDate;
	private Long fromOffice;
	private Long toOffice;
	
	
	public MRNMoveDetailsData(final String serialNumber, final Long mrnId, final Date movedDate) {
		this.serialNumber = serialNumber;
		this.mrnId = mrnId;
		this.movedDate = movedDate;
	}
	
	public MRNMoveDetailsData(final String serialNumber, final Long mrnId, final Date movedDate, final Long fromOffice, final Long toOffice) {
		this.serialNumber = serialNumber;
		this.mrnId = mrnId;
		this.movedDate = movedDate;
		this.fromOffice = fromOffice;
		this.toOffice = toOffice;
	}


	public String getSerialNumber() {
		return serialNumber;
	}


	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}


	public Long getMrnId() {
		return mrnId;
	}


	public void setMrnId(Long mrnId) {
		this.mrnId = mrnId;
	}


	public Date getMovedDate() {
		return movedDate;
	}


	public void setMovedDate(Date movedDate) {
		this.movedDate = movedDate;
	}

	public Long getFromOffice() {
		return fromOffice;
	}

	public void setFromOffice(Long fromOffice) {
		this.fromOffice = fromOffice;
	}

	public Long getToOffice() {
		return toOffice;
	}

	public void setToOffice(Long toOffice) {
		this.toOffice = toOffice;
	}

	public static MRNMoveDetailsData fromJson(JsonCommand command) throws ParseException {
		final Long mrnId = command.longValueOfParameterNamed("mrnId");
		final String serialNumber = command.stringValueOfParameterNamed("serialNumber");
		final String startDateString = command.stringValueOfParameterNamed("movedDate");
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final Date movedDate = df.parse(startDateString);
		return new MRNMoveDetailsData(serialNumber, mrnId, movedDate);
	}
	
	
	
	
}
