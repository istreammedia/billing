package org.mifosplatform.billing.ownedhardware.service;

import java.util.List;

import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.ownedhardware.data.OwnedHardwareData;

public interface OwnedHardwareReadPlatformService {

	public List<OwnedHardwareData> retriveOwnedHardwareData(Long clientId);
	public List<ItemData> retriveTemplate();
	public List<String> retriveSerialNumbers();
	
}
