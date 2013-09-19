package org.mifosplatform.billing.clientprospect.handler;

import org.mifosplatform.billing.clientprospect.service.ClientProspectWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteClientProspectCommandHandler implements NewCommandSourceHandler{

	private ClientProspectWritePlatformService clientProspectWritePlatformService;
	
	@Autowired
	public DeleteClientProspectCommandHandler(final ClientProspectWritePlatformService clientProspectWritePlatformService) {
		this.clientProspectWritePlatformService = clientProspectWritePlatformService;
	}
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return clientProspectWritePlatformService.deleteProspect(command);
	}
}
