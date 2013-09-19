package org.mifosplatform.billing.chargecode.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;


public interface ChargeCodeWritePlatformService {

	public CommandProcessingResult createChargeCode(JsonCommand command);
	public CommandProcessingResult updateChargeCode(JsonCommand command, Long chargeCodeId);
}
