package org.mifosplatform.billing.inventory.mrn.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface MRNDetailsWritePlatformService {

	CommandProcessingResult createMRNDetails(JsonCommand command);

	CommandProcessingResult moveMRN(JsonCommand command, Long mrnId);

}
