package org.mifosplatform.billing.transactionhistory.handler;

import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.stereotype.Service;

@Service
public class TransactionHistoryCommandHandler implements NewCommandSourceHandler{

	
	
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return null;
	}

}
