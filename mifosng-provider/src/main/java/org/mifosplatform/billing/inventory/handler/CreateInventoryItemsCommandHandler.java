package org.mifosplatform.billing.inventory.handler;

import org.mifosplatform.billing.inventory.service.InventoryItemDetailsWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;

@Service
public class CreateInventoryItemsCommandHandler implements NewCommandSourceHandler {
	
	private final InventoryItemDetailsWritePlatformService inventoryItemDetailsWritePlatformService;
	
	
	@Autowired
	public CreateInventoryItemsCommandHandler(final InventoryItemDetailsWritePlatformService inventoryItemDetailsWritePlatformService) {
		this.inventoryItemDetailsWritePlatformService = inventoryItemDetailsWritePlatformService;
	}

	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		
		return this.inventoryItemDetailsWritePlatformService.addItem(command,command.entityId());
	
	}

}
	