package org.mifosplatform.billing.discountmaster.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface DiscountWritePlatformService {

	CommandProcessingResult createNewDiscount(JsonCommand command);

	CommandProcessingResult updateDiscount(Long entityId, JsonCommand command);

	CommandProcessingResult deleteDiscount(Long entityId);

}
