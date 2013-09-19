package org.mifosplatform.billing.address.service;

import java.util.List;

import org.mifosplatform.billing.address.data.AddressData;
import org.mifosplatform.billing.address.data.CountryDetails;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public interface AddressReadPlatformService {


	List<AddressData> retrieveSelectedAddressDetails(String selectedname);

	List<AddressData> retrieveAddressDetails(Long clientId);

	List<AddressData> retrieveAddressDetails();
	
	List<String> retrieveCountryDetails();

	List<String> retrieveStateDetails();

	List<String> retrieveCityDetails();

	List<AddressData> retrieveCityDetails(String selectedname);

	List<EnumOptionData> addressType();

	AddressData retrieveName(String name);

	List<CountryDetails> retrieveCountries();
	
	List<AddressData> retrieveClientAddressDetails(Long clientId);
	

}

