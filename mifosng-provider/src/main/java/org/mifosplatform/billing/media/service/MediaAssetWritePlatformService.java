package org.mifosplatform.billing.media.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface MediaAssetWritePlatformService {


	CommandProcessingResult createMediaAsset(JsonCommand command);

	CommandProcessingResult updateAsset(JsonCommand command);

	CommandProcessingResult deleteAsset(JsonCommand command);

}
