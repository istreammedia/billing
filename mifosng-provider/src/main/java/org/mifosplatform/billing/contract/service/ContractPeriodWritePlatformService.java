package org.mifosplatform.billing.contract.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface ContractPeriodWritePlatformService {

	CommandProcessingResult createContract(JsonCommand command);

	CommandProcessingResult updateContract(Long contractId,JsonCommand command);

	CommandProcessingResult deleteContract(Long entityId);



	

}
