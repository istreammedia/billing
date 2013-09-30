package org.mifosplatform.billing.inventory.mrn.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.data.jpa.domain.AbstractAuditable;

@Entity
@Table(name="b_mrn")
public class MRNDetails extends AbstractAuditableCustom<AppUser, Long>{

	@Column(name="requested_date")
	private Date requestedDate;
	
	@Column(name="item_master_id")
	private Long itemMasterId;
	
	@Column(name="from_office")
	private Long fromOffice;
	
	@Column(name="to_office")
	private Long toOffice;
	
	@Column(name="orderd_quantity")
	private Long orderdQuantity;
	
	@Column(name="received_quantity")
	private Long receivedQuantity=0L;
	
	@Column(name="status")
	private String status="New";
	
	public MRNDetails() {
		
	}
	
	private MRNDetails(final Date requestedDate, final Long fromOffice, final Long toOffice, final Long orderdQuantity, final Long itemMasterId){
		this.requestedDate = requestedDate;
		this.fromOffice = fromOffice;
		this.toOffice = toOffice;
		this.orderdQuantity = orderdQuantity;
		this.itemMasterId = itemMasterId;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
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

	public Long getOrderdQuantity() {
		return orderdQuantity;
	}

	public void setOrderdQuantity(Long orderdQuantity) {
		this.orderdQuantity = orderdQuantity;
	}

	public Long getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Long receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	} 
	
	public static MRNDetails formJson(JsonCommand command) throws ParseException{
//		final Date requestedDate  = command.DateValueOfParameterNamed("requestedDate");
		final Long fromOffice = command.longValueOfParameterNamed("fromOffice");
		final Long toOffice = command.longValueOfParameterNamed("toOffice");
		final Long orderdQuantity = command.bigDecimalValueOfParameterNamed("orderdQuantity").longValue();
		final Long itemMasterId = command.longValueOfParameterNamed("itemDescription");

		final LocalDate requestedDate0=command.localDateValueOfParameterNamed("requestedDate");
		
		String startDateString =requestedDate0.toString()+command.stringValueOfParameterNamed("requestedTime");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date requestedDate = df.parse(startDateString);
			
		return new MRNDetails(requestedDate, fromOffice, toOffice, orderdQuantity, itemMasterId);
	}
	
	
}
