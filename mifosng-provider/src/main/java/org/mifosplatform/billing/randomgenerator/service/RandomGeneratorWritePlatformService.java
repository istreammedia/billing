package org.mifosplatform.billing.randomgenerator.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface RandomGeneratorWritePlatformService {

	CommandProcessingResult createRandomGenerator(JsonCommand command);

	
}
