package org.mifosplatform.billing.batchjobscheduling.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface BatchJobSchedulingWritePlatformService {

	
	public CommandProcessingResult createJobSchedule(JsonCommand command);
}
