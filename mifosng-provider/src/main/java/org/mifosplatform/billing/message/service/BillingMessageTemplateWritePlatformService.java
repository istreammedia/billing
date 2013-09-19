package org.mifosplatform.billing.message.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface BillingMessageTemplateWritePlatformService {
	
CommandProcessingResult addMessageTemplate(JsonCommand json);

CommandProcessingResult updateMessageTemplate(JsonCommand command);

CommandProcessingResult deleteMessageTemplate(JsonCommand command);

}
