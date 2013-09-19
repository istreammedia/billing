package org.mifosplatform.billing.mcodevalues.service;

import java.util.Collection;

import org.mifosplatform.billing.mcodevalues.data.MCodeData;

public interface MCodeReadPlatformService {

	public Collection<MCodeData> getCodeValue(final String codeName);

}
