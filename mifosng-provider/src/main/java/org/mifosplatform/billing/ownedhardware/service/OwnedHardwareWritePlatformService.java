package org.mifosplatform.billing.ownedhardware.service;

import java.util.List;

import org.mifosplatform.billing.ownedhardware.data.OwnedHardwareData;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;


public interface OwnedHardwareWritePlatformService {

	
	public CommandProcessingResult createOwnedHardware(JsonCommand command, Long clientId);
	public List<OwnedHardwareData> getOwnedHardware();
}
