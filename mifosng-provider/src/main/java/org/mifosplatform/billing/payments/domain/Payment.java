package org.mifosplatform.billing.payments.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@Entity
@Table(name = "b_payments")
public class Payment extends AbstractPersistable<Long> {

	@Column(name = "client_id", nullable = false)
	private Long clientId;

	@Column(name = "amount_paid", scale = 6, precision = 19, nullable = false)
	private BigDecimal amountPaid;

	@Column(name = "bill_id", nullable = false)
	private Long statementId;

	@Column(name = "is_deleted", nullable = false)
	private boolean deleted = false;

	@Temporal(TemporalType.DATE)
	@Column(name = "payment_date")
	private Date paymentDate;

	@Column(name = "Remarks")
	private String remarks;

	@Column(name = "paymode_id")
	private int paymode_id;
	

	@Column(name = "transaction_id")
	private String transactionId;


	public Payment() {
	}

	public Payment(final Long clientId, final Long paymentId,final Long externalId, final BigDecimal amountPaid,final Long statmentId, final LocalDate paymentDate,
			final String remark, final Long paymodeCode, String transId) {

		this.clientId = clientId;

		this.statementId = statmentId;
		this.amountPaid = amountPaid;
		this.paymentDate = paymentDate.toDate();
		this.remarks = remark;
		this.paymode_id = paymodeCode.intValue();
		this.transactionId=transId;

	}

	public static Payment fromJson(JsonCommand command, Long clientid) {
		final LocalDate paymentDate = command
				.localDateValueOfParameterNamed("paymentDate");
		final Long paymentCode = command.longValueOfParameterNamed("paymentCode");
				
		final BigDecimal amountPaid = command
				.bigDecimalValueOfParameterNamed("amountPaid");
		final String remarks = command.stringValueOfParameterNamed("remarks");
		final String txtid=command.stringValueOfParameterNamed("txn_id");
		return new Payment(clientid, null, null, amountPaid, null, paymentDate,
				remarks, paymentCode,txtid);

	}

	public Long getClientId() {
		return clientId;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public Long getStatementId() {
		return statementId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public int getPaymodeCode() {
		return paymode_id;
	}

		public void updateBillId(Long billId) {
		this.statementId=billId;

	}

}
