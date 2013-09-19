package org.mifosplatform.billing.region.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface RegionWriteplatformService {

	CommandProcessingResult createNewRegion(JsonCommand command);

	CommandProcessingResult updateRegion(JsonCommand command);

	CommandProcessingResult deleteRegion(Long entityId);



}
