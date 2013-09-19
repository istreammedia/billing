package org.mifosplatform.billing.onetimesale.service;

import java.util.List;

import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.onetimesale.data.AllocationDetailsData;
import org.mifosplatform.billing.onetimesale.data.OneTimeSaleData;

public interface OneTimeSaleReadPlatformService {

	List<ItemData> retrieveItemData();

	List<OneTimeSaleData> retrieveClientOneTimeSalesData(Long clientId);

	List<OneTimeSaleData> retrieveOnetimeSaleDate(Long clientId);

	OneTimeSaleData retrieveSingleOneTimeSaleDetails(Long saleId);

	List<AllocationDetailsData> retrieveAllocationDetails(Long orderId);


}
