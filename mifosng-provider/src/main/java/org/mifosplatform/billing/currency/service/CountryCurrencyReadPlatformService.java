package org.mifosplatform.billing.currency.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.currency.data.CountryCurrencyData;

public interface CountryCurrencyReadPlatformService {

	List<CountryCurrencyData> getTheCountryCurrencyDetaiils(String string);

	Collection<CountryCurrencyData> retrieveCurrencyConfigurationDetails();

	CountryCurrencyData retrieveCurrencyConfigurationDetails(Long id);

}
