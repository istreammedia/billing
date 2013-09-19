package org.mifosplatform.billing.taxmapping.service;

import java.util.List;

import org.mifosplatform.billing.chargecode.data.ChargeCodeData;
import org.mifosplatform.billing.taxmapping.data.TaxMapData;

public interface TaxMapReadPlatformService {

	public List<TaxMapData> retriveTaxMapData(String chargeCode);
	public TaxMapData retriveTaxMapDataForUpdate(final Long id);
	public List<ChargeCodeData> retriveTemplateData();
	public List<TaxMapData> retriveTaxMapTypeData();
}
