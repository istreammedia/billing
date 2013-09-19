package org.mifosplatform.billing.ticketmaster.service;

import java.io.InputStream;

import org.mifosplatform.billing.ticketmaster.command.TicketMasterCommand;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.documentmanagement.command.DocumentCommand;

public interface TicketMasterWritePlatformService {

	/*CommandProcessingResult createTicketMaster(TicketMasterCommand command,
			Long clieniId);*/
	
	CommandProcessingResult createTicketMaster(JsonCommand command);


	Long upDateTicketDetails(
			TicketMasterCommand ticketMasterCommand,
			DocumentCommand documentCommand, Long ticketId,
			InputStream inputStream);

	CommandProcessingResult closeTicket(JsonCommand command);


	String retrieveTicketProblems(Long ticketId);


	
}
