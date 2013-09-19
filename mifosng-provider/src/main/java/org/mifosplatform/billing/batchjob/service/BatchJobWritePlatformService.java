package org.mifosplatform.billing.batchjob.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface BatchJobWritePlatformService {

	
	public CommandProcessingResult creatBatch(JsonCommand command);
}
