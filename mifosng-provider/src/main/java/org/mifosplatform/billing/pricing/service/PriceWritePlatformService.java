package org.mifosplatform.billing.pricing.service;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface PriceWritePlatformService {

	//EntityIdentifier createPricing(final PricingCommand command);

	CommandProcessingResult createPricing(Long planId,JsonCommand command);

	CommandProcessingResult updatePrice(Long priceId, JsonCommand command);

	CommandProcessingResult deletePrice(Long entityId);

	


}
