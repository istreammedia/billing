package org.mifosplatform.billing.selfcare.service;

import org.mifosplatform.billing.selfcare.data.SelfCareData;
import org.mifosplatform.portfolio.client.domain.Client;

public interface SelfCareReadPlatformService {
	
	public Long getClientId(String uniqueReference);

	public String getEmail(Long clientId);
	
	public Long login(String userName, String password);
}
