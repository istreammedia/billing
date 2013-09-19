package org.mifosplatform.billing.adjustment.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.joda.time.LocalDate;
import org.mifosplatform.billing.clientbalance.domain.ClientBalance;
import org.mifosplatform.infrastructure.codes.domain.Code;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;

@Entity
@Table(name = "b_adjustments")
public class Adjustment extends AbstractAuditableCustom<AppUser, Long> {

	@Column(name = "client_id", nullable = false, length = 20)
	private Long client_id;

	@Column(name = "adjustment_date", nullable = false)
	private Date adjustment_date;

	@Column(name = "adjustment_code", nullable = false, length = 10)
	private int adjustment_code;

	@Column(name = "adjustment_type", nullable = false, length = 20)
	private String adjustment_type;

	@Column(name = "adjustment_amount", nullable = false, length = 20)
	private BigDecimal amount_paid;

	@Column(name = "bill_id", nullable = false, length = 20)
	private Long bill_id;

	@Column(name = "external_id", nullable = false, length = 20)
	private Long external_id;


	@Column(name = "remarks", nullable = false, length = 200)
	private String Remarks;

	@OrderBy(value = "id")
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clientId", orphanRemoval = true)
	private List<ClientBalance> clientBalances = new ArrayList<ClientBalance>();

	@OrderBy(value = "id")
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "client_id", orphanRemoval = true)
	private List<Adjustment> adjustment = new ArrayList<Adjustment>();

	
	public static Adjustment fromJson(final JsonCommand command) {
        final LocalDate adjustmentDate = command.localDateValueOfParameterNamed("adjustment_date");
        final Long adjustmentCode = command.longValueOfParameterNamed("adjustment_code");
        final String adjustmentType = command.stringValueOfParameterNamed("adjustment_type");
        final BigDecimal amountPaid = command.bigDecimalValueOfParameterNamed("amount_paid");
        
        final Long billId = command.longValueOfParameterNamed("bill_id");
        final Long externalId = command.longValueOfParameterNamed("external_id");
        final String remarks = command.stringValueOfParameterNamed("Remarks");
        
        return new Adjustment(command.entityId(),adjustmentDate,adjustmentCode,adjustmentType,amountPaid,billId,externalId,remarks);
    }
	
	

	public Adjustment(Long client_id, LocalDate adjustment_date,
			Long adjustment_code, String adjustment_type,
			BigDecimal amount_paid, Long bill_id, Long external_id,
			String Remarks) {
		this.client_id = client_id;
		this.adjustment_date = adjustment_date.toDate();
		this.adjustment_code = (adjustment_code).intValue();
		this.adjustment_type = adjustment_type;
		this.amount_paid = amount_paid;
		this.bill_id = bill_id;
		this.external_id = external_id;
		this.Remarks = Remarks;

	}

	public static Adjustment fromJson(Long client_id, LocalDate adjustment_date,
			Long adjustment_code, String adjustment_type,
			BigDecimal amount_paid, Long bill_id, Long external_id,
			String Remarks) {
		return new Adjustment(client_id, adjustment_date, adjustment_code,
				adjustment_type, amount_paid, bill_id, external_id, Remarks);
	}

	public Adjustment() {

	}

	public void updateclientBalances(ClientBalance clientBalance) {
		clientBalance.updateClient(client_id);
		this.clientBalances.add(clientBalance);

	}

	public void updateAdjustmen(Adjustment adjustment)
	{
		adjustment.updateAdjustmen(adjustment);
		this.adjustment.add(adjustment);
	}


	public List<ClientBalance> getClientBalances() {
		return clientBalances;
	}

	public void updateBillId(Long billId) {
	this.bill_id=billId;

	}



	public Long getClient_id() {
		return client_id;
	}



	public void setClient_id(Long client_id) {
		this.client_id = client_id;
	}



	public Date getAdjustment_date() {
		return adjustment_date;
	}



	public void setAdjustment_date(Date adjustment_date) {
		this.adjustment_date = adjustment_date;
	}



	public Long getAdjustment_code() {
		return Long.valueOf(adjustment_code);
	}



	public void setAdjustment_code(Long adjustment_code) {
		this.adjustment_code = (adjustment_code).intValue();
	}



	public String getAdjustment_type() {
		return adjustment_type;
	}



	public void setAdjustment_type(String adjustment_type) {
		this.adjustment_type = adjustment_type;
	}



	public BigDecimal getAmount_paid() {
		return amount_paid;
	}



	public void setAmount_paid(BigDecimal amount_paid) {
		this.amount_paid = amount_paid;
	}



	public Long getBill_id() {
		return bill_id;
	}



	public void setBill_id(Long bill_id) {
		this.bill_id = bill_id;
	}



	public Long getExternal_id() {
		return external_id;
	}



	public void setExternal_id(Long external_id) {
		this.external_id = external_id;
	}



	public String getRemarks() {
		return Remarks;
	}



	public void setRemarks(String remarks) {
		Remarks = remarks;
	}



	public List<Adjustment> getAdjustment() {
		return adjustment;
	}



	public void setAdjustment(List<Adjustment> adjustment) {
		this.adjustment = adjustment;
	}



	public void setClientBalances(List<ClientBalance> clientBalances) {
		this.clientBalances = clientBalances;
	}
	
	

}