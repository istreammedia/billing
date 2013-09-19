package org.mifosplatform.billing.inventory.mrn.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.inventory.mrn.data.InventoryTransactionHistoryData;
import org.mifosplatform.billing.inventory.mrn.data.MRNDetailsData;

public interface MRNDetailsReadPlatformService {

	List<MRNDetailsData> retriveMRNDetails();

	List<MRNDetailsData> retriveMrnDetailsTemplate();

	List<MRNDetailsData> retriveItemMasterDetails();

	Collection<MRNDetailsData> retriveMrnIds();
	
	List<Long> retriveItemMasterId(Long mrnId);

	List<Long> retriveItemDetailsId(String serialNumber, Long itemMasterId);

	MRNDetailsData retriveFromAndToOffice(Long mrnId);

	List<String> retriveSerialNumbers(Long fromOffice, Long toOffice);

	List<InventoryTransactionHistoryData> retriveHistory();
}
