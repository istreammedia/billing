package org.mifosplatform.billing.randomgenerator.handler;


import org.mifosplatform.billing.randomgenerator.service.RandomGeneratorWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CreateRandomGeneratorCommandHandler implements NewCommandSourceHandler  {

	private final RandomGeneratorWritePlatformService writePlatformService;

    @Autowired
    public CreateRandomGeneratorCommandHandler(final RandomGeneratorWritePlatformService writePlatformService) {
        this.writePlatformService = writePlatformService;
    }

    @Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		
    	return this.writePlatformService.createRandomGenerator(command);
	}
	
}
