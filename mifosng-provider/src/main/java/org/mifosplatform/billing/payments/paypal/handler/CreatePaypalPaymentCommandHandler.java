package org.mifosplatform.billing.payments.paypal.handler;

import org.mifosplatform.billing.payments.service.PaymentWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreatePaypalPaymentCommandHandler implements NewCommandSourceHandler {

	private final PaymentWritePlatformService writePlatformService;

	@Autowired
	public CreatePaypalPaymentCommandHandler(
			final PaymentWritePlatformService writePlatformService) {
		this.writePlatformService = writePlatformService;
	}
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		
		return this.writePlatformService.createPaypalPayment(command);
	}

}
