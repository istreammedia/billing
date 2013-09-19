package org.mifosplatform.billing.clientprospect.handler;

import org.mifosplatform.billing.clientprospect.service.ClientProspectWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class CreateClientProspectCommandHandler implements NewCommandSourceHandler{

	
	private final ClientProspectWritePlatformService prospectWritePlatformService;
	
	@Autowired
	public CreateClientProspectCommandHandler(final ClientProspectWritePlatformService prospectWritePlatformService) {
		this.prospectWritePlatformService = prospectWritePlatformService;
		
	}
	
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return prospectWritePlatformService.createProspect(command);
	}
}
