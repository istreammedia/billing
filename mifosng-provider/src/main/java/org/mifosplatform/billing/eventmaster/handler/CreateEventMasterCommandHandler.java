package org.mifosplatform.billing.eventmaster.handler;

import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.billing.eventmaster.service.EventMasterWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link Service} Class for creating {@link EventMaster}
 * implements {@link NewCommandSourceHandler}
 * 
 * @author pavani
 *
 */
@Service
public class CreateEventMasterCommandHandler implements NewCommandSourceHandler {

	@Autowired
	private EventMasterWritePlatformService eventMasterWritePlatformService;

	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return this.eventMasterWritePlatformService.createEventMaster(command);
	}
}
