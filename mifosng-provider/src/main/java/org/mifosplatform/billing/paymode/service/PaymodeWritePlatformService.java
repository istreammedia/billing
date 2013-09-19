package org.mifosplatform.billing.paymode.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface PaymodeWritePlatformService {

	CommandProcessingResult createPaymode(JsonCommand command);

	CommandProcessingResult updatePaymode(Long codeId, JsonCommand command);

	CommandProcessingResult deletePaymode(Long codeId);

}
