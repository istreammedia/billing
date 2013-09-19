package org.mifosplatform.billing.billmaster.service;

import java.math.BigDecimal;
import java.util.List;

import org.mifosplatform.billing.billingorder.data.BillDetailsData;
import org.mifosplatform.billing.billmaster.domain.BillDetail;
import org.mifosplatform.billing.billmaster.domain.BillMaster;
import org.mifosplatform.billing.financialtransaction.data.FinancialTransactionsData;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface BillWritePlatformService {

	List<BillDetail> createBillDetail(List<FinancialTransactionsData> financialTransactionsDatas,BillMaster master);
	CommandProcessingResult updateBillMaster(List<BillDetail> billDetails,BillMaster billMaster, BigDecimal previousBal);
	String generatePdf(BillDetailsData billDetails,List<FinancialTransactionsData> data);
	void updateBillId(List<FinancialTransactionsData> financialTransactionsDatas, Long billId);
	void ireportPdf(Long billId);


}
