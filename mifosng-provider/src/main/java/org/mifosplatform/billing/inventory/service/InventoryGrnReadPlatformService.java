package org.mifosplatform.billing.inventory.service;

import java.util.Collection;

import org.mifosplatform.billing.inventory.data.InventoryGrnData;

public interface InventoryGrnReadPlatformService {

	public Collection<InventoryGrnData> retriveGrnDetails();
	//public InventoryGrnData retriveGrnDetailTemplate();
	InventoryGrnData retriveGrnDetailTemplate(Long grnId);
	
	public boolean validateForExist(final Long grnId);
	public Collection<InventoryGrnData> retriveGrnIds();
	
}
