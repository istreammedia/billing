package org.mifosplatform.billing.billmaster.service;

import java.math.BigDecimal;
import java.util.List;

import org.mifosplatform.billing.billingorder.data.BillDetailsData;
import org.mifosplatform.billing.financialtransaction.data.FinancialTransactionsData;

public interface BillMasterReadPlatformService {

	List<FinancialTransactionsData> retrieveFinancialData(Long clientId);
	List<FinancialTransactionsData> retrieveInvoiceFinancialData(Long clientId);
	BillDetailsData retrievebillDetails(Long clientId);
	List<FinancialTransactionsData> getFinancialTransactionData(Long id);
	List<FinancialTransactionsData> retrieveStatments(Long clientId);
	BigDecimal retrieveClientBalance(Long clientId);
	List<FinancialTransactionsData> retrieveSingleInvoiceData(Long invoiceId);

}
