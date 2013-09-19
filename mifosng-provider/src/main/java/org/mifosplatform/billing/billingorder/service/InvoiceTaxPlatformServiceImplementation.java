package org.mifosplatform.billing.billingorder.service;
import java.util.ArrayList;
import java.util.List;

import org.mifosplatform.billing.billingorder.commands.InvoiceTaxCommand;
import org.mifosplatform.billing.billingorder.data.ProcessDate;
import org.mifosplatform.billing.billingorder.domain.BillingOrderRepository;
import org.mifosplatform.billing.billingorder.domain.ClientOrderRepository;
import org.mifosplatform.billing.billingorder.domain.InvoiceRepository;
import org.mifosplatform.billing.billingorder.domain.InvoiceTax;
import org.mifosplatform.billing.billingorder.domain.InvoiceTaxRepository;
import org.mifosplatform.billing.clientbalance.domain.ClientBalanceRepository;
import org.mifosplatform.billing.clientbalance.service.UpdateClientBalance;
import org.mifosplatform.billing.order.domain.OrderPriceRepository;
import org.mifosplatform.billing.order.domain.OrderRepository;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceTaxPlatformServiceImplementation implements InvoiceTaxPlatformService {

	private final static Logger logger = LoggerFactory.getLogger(BillingOrderWritePlatformServiceImplementation.class);

	private final PlatformSecurityContext context;
	private final InvoiceTaxRepository invoiceTaxRepository;
	
	@Autowired
	public InvoiceTaxPlatformServiceImplementation(final PlatformSecurityContext context,
			final BillingOrderRepository invoiceChargeRepository,final ClientOrderRepository clientOrderRepository,
			final OrderPriceRepository orderPriceRepository,final InvoiceTaxRepository invoiceTaxRepository,
			final InvoiceRepository invoiceRepository,final BillingOrderReadPlatformService billingProductReadPlatformService,
			final OrderRepository orderRepository,final UpdateClientBalance updateClientBalance,
			final ClientBalanceRepository clientBalanceRepository,
			final ProcessDate processDate
			) {

		this.context = context;
		this.invoiceTaxRepository = invoiceTaxRepository;
	}

	@Override
	public List<InvoiceTax> createInvoiceTax(List<InvoiceTaxCommand> command) {
		List<InvoiceTax> invoiceTaxes = new ArrayList<InvoiceTax>();
		if (command != null) {
			/*for (InvoiceTaxCommand invoiceTaxCommand : command) {
				InvoiceTax invoiceTax = new InvoiceTax(0l, 0l,
						invoiceTaxCommand.getTaxCode(), null,
						invoiceTaxCommand.getTaxPercentage(),
						invoiceTaxCommand.getTaxAmount());
				invoiceTax = this.invoiceTaxRepository.save(invoiceTax);
				invoiceTaxes.add(invoiceTax);
			}*/
		}

		return invoiceTaxes;
	}
}