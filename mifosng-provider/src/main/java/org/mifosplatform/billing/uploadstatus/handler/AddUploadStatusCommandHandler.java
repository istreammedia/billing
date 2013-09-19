package org.mifosplatform.billing.uploadstatus.handler;

import org.mifosplatform.billing.inventory.service.InventoryItemDetailsWritePlatformService;
import org.mifosplatform.billing.uploadstatus.service.UploadStatusWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddUploadStatusCommandHandler implements NewCommandSourceHandler {
	
	private final UploadStatusWritePlatformService uploadStatusWritePlatformService;
	
	
	@Autowired
	public AddUploadStatusCommandHandler(final UploadStatusWritePlatformService uploadStatusWritePlatformService) {
		this.uploadStatusWritePlatformService = uploadStatusWritePlatformService;
	}

	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		//return this.inventoryItemDetailsWritePlatformService.addItem(command);
		return null;
	}

}
