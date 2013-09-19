package org.mifosplatform.billing.contract.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.contract.data.PeriodData;
import org.mifosplatform.billing.contract.data.SubscriptionData;

public interface ContractPeriodReadPlatformService {

	Collection<SubscriptionData> retrieveAllSubscription();

	Collection<SubscriptionData> retrieveSubscriptionDetails();

	SubscriptionData retrieveSubscriptionData(Long subscriptionId);

	List<PeriodData> retrieveAllPlatformPeriod();

}
