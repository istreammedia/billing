package org.mifosplatform.billing.batchjob.handler;

import org.mifosplatform.billing.batchjob.service.BatchJobWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateBatchJobCommandHandler implements NewCommandSourceHandler{

	
	private BatchJobWritePlatformService service;
	
	
	@Autowired
	public CreateBatchJobCommandHandler(final BatchJobWritePlatformService service) {
		this.service = service;
	}
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return service.creatBatch(command);
	}
}
