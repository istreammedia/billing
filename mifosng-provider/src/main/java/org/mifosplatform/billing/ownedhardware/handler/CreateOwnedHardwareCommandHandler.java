package org.mifosplatform.billing.ownedhardware.handler;

import org.mifosplatform.billing.ownedhardware.service.OwnedHardwareWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOwnedHardwareCommandHandler implements NewCommandSourceHandler {

	
	OwnedHardwareWritePlatformService ownedHardwareWritePlatformService;
	
	@Autowired
	public CreateOwnedHardwareCommandHandler(final OwnedHardwareWritePlatformService ownedHardwareWritePlatformService) {
		this.ownedHardwareWritePlatformService = ownedHardwareWritePlatformService;
	}
	
	@Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return ownedHardwareWritePlatformService.createOwnedHardware(command, command.entityId());
	}

}
