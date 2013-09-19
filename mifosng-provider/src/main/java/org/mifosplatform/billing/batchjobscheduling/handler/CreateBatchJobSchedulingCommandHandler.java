package org.mifosplatform.billing.batchjobscheduling.handler;

import org.mifosplatform.billing.batchjobscheduling.service.BatchJobSchedulingWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateBatchJobSchedulingCommandHandler implements NewCommandSourceHandler {

	private BatchJobSchedulingWritePlatformService batchJobSchedulingWritePlatformService;
			
	@Autowired
	public CreateBatchJobSchedulingCommandHandler(final BatchJobSchedulingWritePlatformService batchJobSchedulingWritePlatformService) {
			this.batchJobSchedulingWritePlatformService = batchJobSchedulingWritePlatformService;
	}
	
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {

		return batchJobSchedulingWritePlatformService.createJobSchedule(command);
	}
	
	
}

