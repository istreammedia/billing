package org.mifosplatform.billing.inventory.service;

import org.mifosplatform.billing.inventory.command.ItemDetailsCommand;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

import com.google.gson.JsonElement;

public interface InventoryItemDetailsWritePlatformService {


	CommandProcessingResult addItem(JsonCommand json,Long orderId);
	
	CommandProcessingResult allocateHardware(JsonCommand command);
}
