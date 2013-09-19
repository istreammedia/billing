package org.mifosplatform.billing.billmaster.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;


public interface BillMasterWritePlatformService {

	CommandProcessingResult createBillMaster(JsonCommand command, Long entityId);



}
