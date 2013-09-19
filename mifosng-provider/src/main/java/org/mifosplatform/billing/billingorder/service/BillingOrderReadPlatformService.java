package org.mifosplatform.billing.billingorder.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.billingorder.data.BillingOrderData;
import org.mifosplatform.billing.billingorder.data.GenerateInvoiceData;
import org.mifosplatform.billing.order.data.OrderPriceData;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.billing.taxmaster.data.TaxMappingRateData;

public interface BillingOrderReadPlatformService {

	
	List<TaxMappingRateData> retrieveTaxMappingDate(Long clientId, String chargeCode);

	List<OrderPriceData> retrieveInvoiceTillDate(Long clientOrderId);

	List<BillingOrderData> retrieveBillingOrderData(Long clientId,LocalDate localDate, Long planId);

	List<Long> retrieveOrderIds(Long clientId, LocalDate processDate);

	List<GenerateInvoiceData> retrieveClientsWithOrders(LocalDate processDate);

	List<DiscountMasterData> retrieveDiscountOrders(Long orderId,Long orderPriceId);

	List<TaxMappingRateData> retrieveDefaultTaxMappingDate(Long clientId,
			String chargeCode);

	List<BillingOrderData> getReverseBillingOrderData(Long clientId,LocalDate disconnectionDate, Long orderId);




}
