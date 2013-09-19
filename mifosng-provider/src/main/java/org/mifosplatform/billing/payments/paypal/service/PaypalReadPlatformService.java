package org.mifosplatform.billing.payments.paypal.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;

public interface PaypalReadPlatformService {

	public Long retrieveClientId(JsonCommand command);
}
