package org.mifosplatform.billing.message.handler;


import org.mifosplatform.billing.message.service.BillingMessageDataWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CreateMessageDataCommandHandler implements NewCommandSourceHandler{
	
	private final BillingMessageDataWritePlatformService billingmessageDataWritePlatformService;
	
	@Autowired
	public CreateMessageDataCommandHandler(
			final BillingMessageDataWritePlatformService billingmessageDataWritePlatformService)
	{
	this.billingmessageDataWritePlatformService =billingmessageDataWritePlatformService;
	}

	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		// TODO Auto-generated method stub
		return this.billingmessageDataWritePlatformService.createMessageData(command.entityId(),command.json());
	}

}
