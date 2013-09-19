package org.mifosplatform.billing.eventorder.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface EventOrderWriteplatformService {

	CommandProcessingResult createEventOrder(JsonCommand command);

}
