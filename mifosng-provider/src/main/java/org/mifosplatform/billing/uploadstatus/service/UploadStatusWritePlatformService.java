package org.mifosplatform.billing.uploadstatus.service;

import org.mifosplatform.billing.uploadstatus.command.UploadStatusCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;


public interface UploadStatusWritePlatformService {


	CommandProcessingResult addItem(UploadStatusCommand command);
	CommandProcessingResult updateUploadStatus(Long orderId,ApiRequestJsonSerializationSettings settings);
	UploadStatusCommand convertJsonToUploadStatusCommand(Object object,String jsonRequestBody);
	
	
	

}
