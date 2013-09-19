package org.mifosplatform.billing.clientbalance.service;


import java.util.List;

import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;


public interface ClientBalanceReadPlatformService {

	
		ClientBalanceData retrieveBalance(Long clientid);

		List<ClientBalanceData> retrieveAllClientBalances(Long clientId);

	
	}

