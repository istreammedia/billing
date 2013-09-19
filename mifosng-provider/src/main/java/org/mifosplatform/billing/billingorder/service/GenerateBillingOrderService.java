package org.mifosplatform.billing.billingorder.service;

import java.math.BigDecimal;
import java.util.List;

import org.mifosplatform.billing.billingorder.commands.BillingOrderCommand;
import org.mifosplatform.billing.billingorder.commands.InvoiceCommand;
import org.mifosplatform.billing.billingorder.commands.InvoiceTaxCommand;
import org.mifosplatform.billing.billingorder.data.BillingOrderData;
import org.mifosplatform.billing.billingorder.domain.Invoice;
import org.mifosplatform.billing.taxmaster.data.TaxMappingRateData;

public interface GenerateBillingOrderService {

	public List<BillingOrderCommand> generatebillingOrder(List<BillingOrderData> products);

	public List<InvoiceTaxCommand> generateInvoiceTax(List<TaxMappingRateData> taxMappingRateDatas,BigDecimal price,Long clientId);

	public Invoice generateInvoice(List<BillingOrderCommand> billingOrderCommands);

	
}
