package org.mifosplatform.billing.payments.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface PaymentWritePlatformService {

	CommandProcessingResult createPayment(JsonCommand command);

	Long createPayments(Long clientBalanceid, Long clientid, JsonCommand command);

	CommandProcessingResult createPaypalPayment(JsonCommand command);

	
	
	
	

}
