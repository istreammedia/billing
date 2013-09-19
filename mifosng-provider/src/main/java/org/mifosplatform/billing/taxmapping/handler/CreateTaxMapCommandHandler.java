package org.mifosplatform.billing.taxmapping.handler;

import org.mifosplatform.billing.taxmapping.service.TaxMapWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTaxMapCommandHandler implements NewCommandSourceHandler{

	
	private final TaxMapWritePlatformService taxMapWritePlatformService;  
	
	@Autowired
	CreateTaxMapCommandHandler(final TaxMapWritePlatformService taxMapWritePlatformService){
		this.taxMapWritePlatformService =  taxMapWritePlatformService;
	}
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return taxMapWritePlatformService.createTaxMap(command);
	}


}
