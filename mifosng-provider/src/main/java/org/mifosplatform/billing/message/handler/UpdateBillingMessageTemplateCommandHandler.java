package org.mifosplatform.billing.message.handler;

import org.mifosplatform.billing.message.service.BillingMessageTemplateWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UpdateBillingMessageTemplateCommandHandler implements NewCommandSourceHandler {

private final BillingMessageTemplateWritePlatformService billingMessageTemplateWritePlatformService;
	
	@Autowired
	public UpdateBillingMessageTemplateCommandHandler(
			final BillingMessageTemplateWritePlatformService billingMessageTemplateWritePlatformService)
	{
	this.billingMessageTemplateWritePlatformService =billingMessageTemplateWritePlatformService;
	}

	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		// TODO Auto-generated method stub
		return this.billingMessageTemplateWritePlatformService.updateMessageTemplate(command);
	}

}
