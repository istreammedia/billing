/**
 * 
 */
package org.mifosplatform.billing.ticketmaster.handler;

import org.mifosplatform.billing.ticketmaster.service.TicketMasterWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nookala
 *
 */

@Service
public class DeleteTicketMasterCommandHandler implements NewCommandSourceHandler  {
	
	private final TicketMasterWritePlatformService ticketMasterWriteService;
	
	@Autowired
	public DeleteTicketMasterCommandHandler (final TicketMasterWritePlatformService ticketMasterWriteService) {
		this.ticketMasterWriteService = ticketMasterWriteService;
	}
	
	@Transactional
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.ticketMasterWriteService.closeTicket(command);
    }
}
