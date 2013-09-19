package org.mifosplatform.billing.servicemaster.service;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface ServiceMasterWritePlatformService {

	public CommandProcessingResult updateService(Long serviceId, JsonCommand command);

	public CommandProcessingResult deleteService(Long serviceId);

	public CommandProcessingResult createNewService(JsonCommand command);

}
