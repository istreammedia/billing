package org.mifosplatform.billing.billingorder.service;

import java.util.List;

import org.mifosplatform.billing.billingorder.commands.BillingOrderCommand;
import org.mifosplatform.billing.billingorder.commands.InvoiceCommand;
import org.mifosplatform.billing.billingorder.commands.InvoiceTaxCommand;
import org.mifosplatform.billing.billingorder.domain.BillingOrder;
import org.mifosplatform.billing.billingorder.domain.Invoice;
import org.mifosplatform.billing.billingorder.domain.InvoiceTax;
import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface BillingOrderWritePlatformService {

	List<BillingOrder> createBillingProduct(List<BillingOrderCommand> billingOrderCommands);
	CommandProcessingResult updateBillingOrder(List<BillingOrderCommand> billingOrderCommands);
	CommandProcessingResult updateOrderPrice(List<BillingOrderCommand> billingOrderCommands);
	List<InvoiceTax> createInvoiceTax(List<InvoiceTaxCommand> command);
	Invoice createInvoice(InvoiceCommand command,List<ClientBalanceData> clientBalanceDatas);
	public void updateInvoiceTax(Invoice invoice,List<BillingOrderCommand> billingOrderCommands,List<BillingOrder> billingOrders);
	void updateInvoiceCharge(Invoice invoice,List<BillingOrder>  billingOrder);
	void updateClientBalance(Invoice invoice,List<ClientBalanceData> clientBalancesDatas);

}
