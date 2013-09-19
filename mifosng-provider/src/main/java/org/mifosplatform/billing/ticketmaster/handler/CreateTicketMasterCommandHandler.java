package org.mifosplatform.billing.ticketmaster.handler;

import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.billing.ticketmaster.service.TicketMasterWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nookala
 *
 */
@Service
public class CreateTicketMasterCommandHandler implements NewCommandSourceHandler {
	
	private final TicketMasterWritePlatformService ticketMasterWriteService;
	
	@Autowired
	public CreateTicketMasterCommandHandler (final TicketMasterWritePlatformService ticketMasterWriteService) {
		this.ticketMasterWriteService = ticketMasterWriteService;
	}
	
	@Transactional
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.ticketMasterWriteService.createTicketMaster(command);
    }
}
