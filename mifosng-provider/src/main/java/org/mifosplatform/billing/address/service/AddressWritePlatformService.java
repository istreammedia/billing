package org.mifosplatform.billing.address.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface AddressWritePlatformService {
	
	

	CommandProcessingResult updateAddress(Long addrId, JsonCommand command);

	CommandProcessingResult createNewRecord(JsonCommand command,String entityType);

	CommandProcessingResult createAddress(Long clientId, JsonCommand command);

}
