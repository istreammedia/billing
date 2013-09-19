package org.mifosplatform.billing.billingorder.commands;

import java.math.BigDecimal;

public class InvoiceTaxCommand {

	private final Long clientId;
	private final Long invoiceChargeId;
	private final Long invoiceId;
	private final String taxCode;
	private final BigDecimal taxValue;
	private final BigDecimal taxPercentage;
	private final BigDecimal taxAmount;

	public InvoiceTaxCommand(final Long clientId, final Long invoiceChargeId,
			final Long invoiceId, final String taxCode,
			final BigDecimal taxValue, final BigDecimal taxPercentage,
			final BigDecimal taxAmount) {

		this.clientId = clientId;
		this.invoiceChargeId = invoiceChargeId;
		this.invoiceId = invoiceId;
		this.taxCode = taxCode;
		this.taxValue = taxValue;
		this.taxPercentage = taxPercentage;
		this.taxAmount = taxAmount;
	}

	public Long getClientId() {
		return clientId;
	}

	public Long getInvoiceChargeId() {
		return invoiceChargeId;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public BigDecimal getTaxValue() {
		return taxValue;
	}

	public BigDecimal getTaxPercentage() {
		return taxPercentage;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

}
