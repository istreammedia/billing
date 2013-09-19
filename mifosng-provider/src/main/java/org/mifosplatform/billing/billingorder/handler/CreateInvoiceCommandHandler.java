package org.mifosplatform.billing.billingorder.handler;

import org.mifosplatform.billing.billingorder.service.InvoiceClient;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateInvoiceCommandHandler  implements NewCommandSourceHandler {
	
	private final InvoiceClient invoiceClient;
	
	@Autowired
    public CreateInvoiceCommandHandler(final InvoiceClient invoiceClient) {
        this.invoiceClient = invoiceClient;
    }
	
    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.invoiceClient.createInvoiceBill(command);
    }
    

}
