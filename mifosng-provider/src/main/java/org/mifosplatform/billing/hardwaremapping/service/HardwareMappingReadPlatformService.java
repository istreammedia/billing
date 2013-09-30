package org.mifosplatform.billing.hardwaremapping.service;

import java.util.List;

import org.mifosplatform.billing.hardwaremapping.data.HardwareMappingDetailsData;

public interface HardwareMappingReadPlatformService {

	List<HardwareMappingDetailsData> getPlanDetailsByItemCode(String itemCode, Long clientId);

}
