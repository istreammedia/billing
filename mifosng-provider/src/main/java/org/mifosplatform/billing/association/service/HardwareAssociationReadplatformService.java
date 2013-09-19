package org.mifosplatform.billing.association.service;

import java.util.List;

import org.mifosplatform.billing.association.data.HardwareAssociationData;


public interface HardwareAssociationReadplatformService {

	List<HardwareAssociationData> retrieveClientHardwareDetails(Long clientId);

	List<HardwareAssociationData> retrieveClientUnallocatePlanDetails(Long clientId);

}
