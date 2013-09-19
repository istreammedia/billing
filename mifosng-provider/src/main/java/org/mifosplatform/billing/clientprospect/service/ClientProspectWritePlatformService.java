package org.mifosplatform.billing.clientprospect.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface ClientProspectWritePlatformService {

	public CommandProcessingResult createProspect(JsonCommand command);
	public CommandProcessingResult updateProspect(JsonCommand command, final Long prospectId);
	public CommandProcessingResult deleteProspect(JsonCommand command);
	public CommandProcessingResult convertToClient(Long entityId);
	
}
