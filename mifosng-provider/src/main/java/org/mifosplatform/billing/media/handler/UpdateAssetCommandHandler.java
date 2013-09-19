package org.mifosplatform.billing.media.handler;

import org.mifosplatform.billing.media.service.MediaAssetWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateAssetCommandHandler implements NewCommandSourceHandler {

	private final MediaAssetWritePlatformService writePlatformService;

    @Autowired
    public UpdateAssetCommandHandler(final MediaAssetWritePlatformService writePlatformService) {
        this.writePlatformService = writePlatformService;
    }
	
    @Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		
		return this.writePlatformService.updateAsset(command);
	}

}
