package org.mifosplatform.billing.chargecode.service;

import java.util.List;

import org.mifosplatform.billing.chargecode.data.BillFrequencyCodeData;
import org.mifosplatform.billing.chargecode.data.ChargeCodeData;
import org.mifosplatform.billing.chargecode.data.ChargeTypeData;
import org.mifosplatform.billing.chargecode.data.DurationTypeData;

public interface ChargeCodeReadPlatformService {

	
	List<ChargeCodeData> getChargeCode();
	List<ChargeTypeData> getChargeType();
	List<DurationTypeData> getDurationType();
	List<BillFrequencyCodeData> getBillFrequency();
	ChargeCodeData getChargeCode(Long chargeCodeId);
}
