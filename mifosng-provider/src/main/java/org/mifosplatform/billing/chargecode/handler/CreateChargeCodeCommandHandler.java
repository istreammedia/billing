package org.mifosplatform.billing.chargecode.handler;

import org.mifosplatform.billing.chargecode.service.ChargeCodeWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateChargeCodeCommandHandler implements NewCommandSourceHandler{

	private final ChargeCodeWritePlatformService chargeCodeWritePlatformService;
	
	@Autowired
	public CreateChargeCodeCommandHandler(final ChargeCodeWritePlatformService chargeCodeWritePlatformService) {
		this.chargeCodeWritePlatformService = chargeCodeWritePlatformService;
	}

	@Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return chargeCodeWritePlatformService.createChargeCode(command);
	}
	
	
}
