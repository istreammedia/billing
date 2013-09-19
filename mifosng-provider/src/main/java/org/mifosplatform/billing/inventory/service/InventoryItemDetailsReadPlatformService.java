package org.mifosplatform.billing.inventory.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.inventory.data.AllocationHardwareData;
import org.mifosplatform.billing.inventory.data.InventoryItemDetailsData;
import org.mifosplatform.billing.inventory.data.InventoryItemSerialNumberData;
import org.mifosplatform.billing.inventory.data.ItemMasterIdData;
import org.mifosplatform.billing.inventory.data.QuantityData;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetails;

public interface InventoryItemDetailsReadPlatformService {

	
	public Collection<InventoryItemDetailsData> retriveAllItemDetails();
	
	public InventoryItemDetailsData retriveIndividualItemDetails();

	public List<QuantityData> retriveSerialNumbers(Long oneTimeSaleId);
	
	public QuantityData retriveQuantity(Long oneTimeSaleId);
	
	public ItemMasterIdData retriveItemMasterId(Long oneTimeSaleId);
	
	public List<Long> retriveSerialNumberForItemMasterId(String serialNumber);

	public InventoryItemSerialNumberData retriveAllocationData(List<QuantityData> itemSerialNumbers,QuantityData quantityData, ItemMasterIdData itemMasterIdData);
	
	public InventoryItemDetails retriveInventoryItemDetail(String serialNumber,Long itemMasterId);

	List<String> retriveSerialNumbers();
}
