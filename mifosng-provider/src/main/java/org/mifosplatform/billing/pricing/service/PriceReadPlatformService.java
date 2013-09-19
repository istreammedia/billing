package org.mifosplatform.billing.pricing.service;

import java.util.List;

import org.mifosplatform.billing.charge.data.ChargesData;
import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.billing.plan.data.PlanData;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.pricing.data.PricingData;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public interface PriceReadPlatformService {

	 List<ChargesData> retrieveChargeCode();
     List<EnumOptionData> retrieveChargeVariantData();
	List<DiscountMasterData> retrieveDiscountDetails();
//	List<PlanData> retrievePlanDetails();
	List<SubscriptionData> retrievePaytermData();
	List<ServiceData> retrievePriceDetails(String planCode);
	List<ServiceData> retrievePrcingDetails(Long planId);
	List<ServiceData> retrieveServiceCodeDetails(Long planCode);
	PricingData retrieveSinglePriceDetails(String priceId);
}
