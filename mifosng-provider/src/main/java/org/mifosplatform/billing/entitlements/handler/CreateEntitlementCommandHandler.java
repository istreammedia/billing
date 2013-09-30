package org.mifosplatform.billing.entitlements.handler;

import org.mifosplatform.billing.entitlements.service.EntitlementWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateEntitlementCommandHandler implements NewCommandSourceHandler {

	private final EntitlementWritePlatformService writePlatformService;
	
	@Autowired
	public CreateEntitlementCommandHandler(EntitlementWritePlatformService writePlatformService){
		this.writePlatformService=writePlatformService;
		
	}

	@Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return this.writePlatformService.create(command);
	}
}
