package org.mifosplatform.billing.billingorder.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.billingorder.commands.BillingOrderCommand;
import org.mifosplatform.billing.billingorder.data.BillingOrderData;
import org.mifosplatform.billing.billingorder.domain.Invoice;

public interface GenerateReverseBillingOrderService {

	List<BillingOrderCommand> generateReverseBillingOrder(List<BillingOrderData> billingOrderProducts,
			LocalDate disconnectDate);

	Invoice generateNegativeInvoice(List<BillingOrderCommand> billingOrderCommands);

}
