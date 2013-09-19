package org.mifosplatform.billing.eventorder.service;

import java.util.List;

import org.mifosplatform.billing.eventorder.data.EventOrderData;
import org.mifosplatform.billing.onetimesale.data.OneTimeSaleData;

public interface EventOrderReadplatformServie {
	
	List<OneTimeSaleData> retrieveEventOrderData(Long clientId);

	boolean CheckClientCustomalidation(Long clientId);

	List<EventOrderData> getTheClientEventOrders(Long clientId);

}
