package org.mifosplatform.billing.association.service;

public interface HardwareAssociationWriteplatformService {

	void createNewHardwareAssociation(Long clientId, Long id, String serialNo, Long orderId);

}
