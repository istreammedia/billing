package org.mifosplatform.billing.chargecode.handler;

import org.mifosplatform.billing.chargecode.service.ChargeCodeWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateChargeCodeCommandHandler implements NewCommandSourceHandler{

	
	private final ChargeCodeWritePlatformService chargeCodeWritePlatformService;
	
	@Autowired
	public UpdateChargeCodeCommandHandler(final ChargeCodeWritePlatformService chargeCodeWritePlatformService) {
		this.chargeCodeWritePlatformService = chargeCodeWritePlatformService;
	}

	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return chargeCodeWritePlatformService.updateChargeCode(command,command.entityId());
	}
	
	
	
	
	
	
}
