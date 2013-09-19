package org.mifosplatform.billing.eventpricing.handler;

import org.mifosplatform.billing.eventpricing.domain.EventPricing;
import org.mifosplatform.billing.eventpricing.service.EventPricingWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link Service} Class for Creating {@link EventPricing} handler
 * implements {@link NewCommandSourceHandler}
 * 
 * @author pavani
 *
 */
@Service
public class CreateEventPriceCommandHandler implements NewCommandSourceHandler {

	@Autowired
	private EventPricingWritePlatformService eventPricingWritePlatformService;
	
	
    @Transactional
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.eventPricingWritePlatformService.createEventPricing(command);
    }

}
