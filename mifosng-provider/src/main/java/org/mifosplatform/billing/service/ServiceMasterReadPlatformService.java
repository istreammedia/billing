package org.mifosplatform.billing.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.servicemaster.data.ServiceMasterOptionsData;
import org.mifosplatform.billing.servicemaster.data.ServiceMasterData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public interface ServiceMasterReadPlatformService {
	 Collection<ServiceMasterData> retrieveAllServiceMasterData() ;

	List<ServiceMasterOptionsData> retrieveServices();

	ServiceMasterOptionsData retrieveIndividualService(Long serviceId);

	List<EnumOptionData> retrieveServicesTypes();

	List<EnumOptionData> retrieveServiceUnitType();
}
