package org.mifosplatform.billing.entitlements.service;

import java.util.List;

import org.mifosplatform.billing.entitlements.data.EntitlementsData;


public interface EntitlementReadPlatformService {
	

	List<EntitlementsData> getProcessingData(Long id);

	
}
