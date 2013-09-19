package org.mifosplatform.billing.inventory.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface InventoryGrnDetailsWritePlatformService {


	CommandProcessingResult addGrnDetails(JsonCommand command);
	
}
