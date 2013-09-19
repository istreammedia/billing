package org.mifosplatform.billing.plan.service;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface PlanWritePlatformService {

	CommandProcessingResult createPlan(JsonCommand command);

	CommandProcessingResult updatePlan(Long entityId, JsonCommand command);

	CommandProcessingResult deleteplan(Long entityId);

	

}
