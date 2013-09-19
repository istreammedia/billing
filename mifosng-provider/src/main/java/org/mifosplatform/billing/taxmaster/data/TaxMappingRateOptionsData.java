package org.mifosplatform.billing.taxmaster.data;

import java.util.Collection;

import org.mifosplatform.billing.charge.data.ChargeCodeData;
import org.mifosplatform.billing.discountmaster.commands.DiscountValues;

public class TaxMappingRateOptionsData {
	private Collection<DiscountValues> datass;
	private Collection<TaxMasterData> taxMasterData;
	private Collection<ChargeCodeData> chargeCodeData;

	public TaxMappingRateOptionsData(Collection<ChargeCodeData> chargeCodeData,Collection<TaxMasterData> taxMasterData,Collection<DiscountValues> datass)
	{
		this.taxMasterData=taxMasterData;
		this.chargeCodeData=chargeCodeData;
		this.datass=datass;

	}
}
