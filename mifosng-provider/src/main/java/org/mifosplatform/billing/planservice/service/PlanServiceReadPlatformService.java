package org.mifosplatform.billing.planservice.service;

import java.util.Collection;

import org.mifosplatform.billing.planservice.data.PlanServiceData;

public interface PlanServiceReadPlatformService {

	Collection<PlanServiceData> retrieveClientPlanService(Long clientId,
			String serviceType);

}
