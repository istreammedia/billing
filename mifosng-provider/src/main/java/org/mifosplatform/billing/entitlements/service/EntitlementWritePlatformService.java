package org.mifosplatform.billing.entitlements.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface EntitlementWritePlatformService {

	public CommandProcessingResult create(JsonCommand command);
}
