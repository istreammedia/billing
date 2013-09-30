package org.mifosplatform.billing.order.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface OrderWritePlatformService {
	CommandProcessingResult createOrder(Long entityId, JsonCommand command);
	CommandProcessingResult updateOrderPrice(Long orderId, JsonCommand command);
	CommandProcessingResult deleteOrder(Long orderId,JsonCommand command);
	//CommandProcessingResult updateOrder(JsonCommand command);
	CommandProcessingResult renewalClientOrder(JsonCommand command);
	CommandProcessingResult reconnectOrder(Long entityId);
//	CommandProcessingResult updateOrder(JsonCommand command, Long orderId);
	CommandProcessingResult disconnectOrder(JsonCommand command, Long orderId);
}
