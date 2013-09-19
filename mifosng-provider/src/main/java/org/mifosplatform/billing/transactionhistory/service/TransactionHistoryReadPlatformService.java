package org.mifosplatform.billing.transactionhistory.service;

import java.util.List;

import org.mifosplatform.billing.transactionhistory.data.TransactionHistoryData;

public interface TransactionHistoryReadPlatformService {

	public List<TransactionHistoryData> retriveTransactionHistoryTemplate();
	public List<TransactionHistoryData> retriveTransactionHistoryById(Long clientId);
}
