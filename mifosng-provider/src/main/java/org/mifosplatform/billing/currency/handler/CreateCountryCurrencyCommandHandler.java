package org.mifosplatform.billing.currency.handler;

import org.mifosplatform.billing.currency.service.CountryCurrencyWritePlatformService;
import org.mifosplatform.billing.ownedhardware.service.OwnedHardwareWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCountryCurrencyCommandHandler implements NewCommandSourceHandler {

	
	CountryCurrencyWritePlatformService countryCurrencyWritePlatformService;
	
	@Autowired
	public CreateCountryCurrencyCommandHandler(final CountryCurrencyWritePlatformService countryCurrencyWritePlatformService) {
		this.countryCurrencyWritePlatformService=countryCurrencyWritePlatformService;
	}
	
	@Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return countryCurrencyWritePlatformService.createCountryCurrency(command);
	}

}
