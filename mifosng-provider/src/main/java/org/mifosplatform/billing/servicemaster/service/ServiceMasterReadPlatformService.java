package org.mifosplatform.billing.servicemaster.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.servicemaster.data.ServiceMasterOptionsData;
import org.mifosplatform.billing.servicemaster.data.ServiceMasterData;

public interface ServiceMasterReadPlatformService {
	 Collection<ServiceMasterData> retrieveAllServiceMasterData() ;

	List<ServiceMasterOptionsData> retrieveServices();

	ServiceMasterOptionsData retrieveIndividualService(Long serviceId);
}
