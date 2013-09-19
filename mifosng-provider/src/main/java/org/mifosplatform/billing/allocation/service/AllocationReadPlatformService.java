package org.mifosplatform.billing.allocation.service;

import java.util.List;

import org.mifosplatform.billing.onetimesale.data.AllocationDetailsData;

public interface AllocationReadPlatformService {

	AllocationDetailsData getTheHardwareItemDetails(Long clientId);

	List<AllocationDetailsData> retrieveHardWareDetailsByItemCode(Long clientId, String itemCode);

}
