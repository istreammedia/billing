package org.mifosplatform.billing.onetimesale.service;

import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.api.JsonQuery;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface OneTimeSaleWritePlatformService {

	CommandProcessingResult createOneTimeSale(JsonCommand command,Long clientId);

	ItemData calculatePrice(Long itemId, JsonQuery query);

}
