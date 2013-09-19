package org.mifosplatform.billing.adjustment.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;


public interface AdjustmentWritePlatformService {
	public Long createAdjustment(final Long id2,final Long id,final Long clientid,final JsonCommand command);
	
	public CommandProcessingResult createAdjustments(JsonCommand command);

}
