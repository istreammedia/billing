
package org.mifosplatform.billing.onetimesale.handler;

import org.mifosplatform.billing.onetimesale.service.OneTimeSaleWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOneTimeSaleCommandHandler implements NewCommandSourceHandler {

    private final OneTimeSaleWritePlatformService writePlatformService;

    @Autowired
    public CreateOneTimeSaleCommandHandler(final OneTimeSaleWritePlatformService writePlatformService) {
        this.writePlatformService = writePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.writePlatformService.createOneTimeSale(command,command.entityId());
    }
}