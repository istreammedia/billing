package org.mifosplatform.billing.priceregion.service;

import java.util.List;

import org.mifosplatform.billing.priceregion.data.PriceRegionData;

public interface RegionalPriceReadplatformService {

	List<PriceRegionData> getThePriceregionsDetails();

	PriceRegionData getTheClientRegionDetails(Long clientId);


}
