package org.mifosplatform.billing.pricing.handler;

import org.mifosplatform.billing.contract.service.ContractPeriodWritePlatformService;
import org.mifosplatform.billing.pricing.service.PriceWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.codes.service.CodeWritePlatformService;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeletePriceCommandHandler implements NewCommandSourceHandler {

    private final  PriceWritePlatformService writePlatformService;

    @Autowired
    public DeletePriceCommandHandler(final PriceWritePlatformService writePlatformService) {
        this.writePlatformService = writePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.writePlatformService.deletePrice(command.entityId());
    }
}