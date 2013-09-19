package org.mifosplatform.billing.item.handler;

import org.mifosplatform.billing.item.service.ItemWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateItemCommandHandler implements NewCommandSourceHandler {

	
	private ItemWritePlatformService itemWritePlatformService;
	
	@Autowired
    public CreateItemCommandHandler(final ItemWritePlatformService itemWritePlatformService) {
        this.itemWritePlatformService = itemWritePlatformService;
    }
	
	@Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		
		return this.itemWritePlatformService.createItem(command);
	}

}
