package org.mifosplatform.billing.eventorder.service;

import org.mifosplatform.billing.order.domain.Order;
import org.mifosplatform.billing.plan.domain.Plan;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface PrepareRequestWriteplatformService {

	CommandProcessingResult prepareNewRequest(Order order,Plan provisioningStatus, String requstStatus);

}
