package org.mifosplatform.billing.eventpricing.service;

import org.mifosplatform.billing.eventpricing.domain.EventPricing;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

/**
 * Interface for {@link EventPricing} Write Service
 * 
 * @author pavani
 *
 */
public interface EventPricingWritePlatformService {

	/**
	 * Method for Creating {@link EventPricing}
	 * 
	 * @param command
	 * @return
	 */
	CommandProcessingResult createEventPricing(JsonCommand command);
	
	/**
	 * Method for Updating {@link EventPricing}
	 * 
	 * @param command
	 * @return
	 */
	CommandProcessingResult updateEventPricing(JsonCommand command);
	
	/**
	 * Method for Deleting EventPricing
	 * 
	 * @param command
	 * @return
	 */
	CommandProcessingResult deleteEventPricing(JsonCommand command);
	
}
