package org.mifosplatform.billing.region.handler;

import org.mifosplatform.billing.region.service.RegionWriteplatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateRegionCommandHandler implements NewCommandSourceHandler {

	private RegionWriteplatformService regionWriteplatformService;
	
	@Autowired
	public CreateRegionCommandHandler(final RegionWriteplatformService regionWriteplatformService) {
		this.regionWriteplatformService = regionWriteplatformService;
	}
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return this.regionWriteplatformService.createNewRegion(command);
	}

}

