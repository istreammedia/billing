/**
 * 
 */
package org.mifosplatform.billing.eventpricing.handler;

import org.mifosplatform.billing.eventpricing.domain.EventPricing;
import org.mifosplatform.billing.eventpricing.service.EventPricingWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link Service} Class Deleting {@link EventPricing} Handler
 * implements {@link NewCommandSourceHandler}
 * 
 * @author pavani
 *
 */
@Service
public class CloseEventPriceCommandHandler implements NewCommandSourceHandler {
	
	@Autowired
	private EventPricingWritePlatformService eventPricingWritePlatformService;
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return this.eventPricingWritePlatformService.deleteEventPricing(command);
	}

}
