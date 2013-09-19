package org.mifosplatform.billing.taxmaster.data;

import java.util.Collection;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class TaxMasterDataOptions {
	private Collection<EnumOptionData> enumOptionData;

	public TaxMasterDataOptions(Collection<EnumOptionData> enumOptionData)
	{
		this.enumOptionData=enumOptionData;
	}
}
