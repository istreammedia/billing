package org.mifosplatform.billing.region.service;

import java.util.List;

import org.mifosplatform.billing.address.data.StateDetails;
import org.mifosplatform.billing.region.data.RegionData;
import org.mifosplatform.billing.region.data.RegionDetailsData;

public interface RegionReadPlatformService {

	List<StateDetails> getAvailableStates(Long countryId);

	List<RegionData> getRegionDetails();

	RegionData getSingleRegionDetails(Long regionId);

	List<RegionDetailsData> getRegionDetailsData(Long regionId);
	List<RegionDetailsData> getCountryRegionDetails(Long regionId,Long stateId);

}
