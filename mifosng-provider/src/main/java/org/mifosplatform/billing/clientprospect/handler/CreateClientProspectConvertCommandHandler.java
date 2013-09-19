package org.mifosplatform.billing.clientprospect.handler;

import org.mifosplatform.billing.clientprospect.service.ClientProspectWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateClientProspectConvertCommandHandler implements NewCommandSourceHandler{

private final ClientProspectWritePlatformService prospectWritePlatformService;
	
	@Autowired
	public CreateClientProspectConvertCommandHandler(final ClientProspectWritePlatformService prospectWritePlatformService) {
		this.prospectWritePlatformService = prospectWritePlatformService;
		
	}
	
	@Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return prospectWritePlatformService.convertToClient(command.entityId());
	}
	
}
