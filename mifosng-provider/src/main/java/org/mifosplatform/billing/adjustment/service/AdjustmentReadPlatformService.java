package org.mifosplatform.billing.adjustment.service;

import java.util.List;

import org.mifosplatform.billing.adjustment.data.AdjustmentData;
import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;

public interface AdjustmentReadPlatformService {
	
	List<ClientBalanceData> retrieveAllAdjustments(Long id);

	List<AdjustmentData> retrieveAllAdjustmentsCodes();

	
}
