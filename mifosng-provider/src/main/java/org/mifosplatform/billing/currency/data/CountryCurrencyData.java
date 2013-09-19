package org.mifosplatform.billing.currency.data;

import java.util.List;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.organisation.monetary.data.ApplicationCurrencyConfigurationData;

public class CountryCurrencyData {
	
	private  Long id;
	private  String country;
	private  String currency;
	private  String status;
	private ApplicationCurrencyConfigurationData currencydata;
	private List<String> countryData;
	private List<EnumOptionData> currencystatus;
	

	public CountryCurrencyData(Long id, String country, String currency, String status) {
	    
		this.id=id;
		this.country=country;
		this.currency=currency;
		this.status=status;
           
	}


	public CountryCurrencyData(CountryCurrencyData currencyData,ApplicationCurrencyConfigurationData currency,
			List<String> countryData, List<EnumOptionData> status) {
                 
		if(currencyData!=null){
			this.id=currencyData.getId();
			this.country=currencyData.getCountry();
			this.currency=currencyData.getCurrency();
			this.status=currencyData.getStatus();
		}

	          this.currencydata=currency;
	          this.countryData=countryData;
	          this.currencystatus=status;
	}


	public Long getId() {
		return id;
	}


	public String getCountry() {
		return country;
	}


	public String getCurrency() {
		return currency;
	}


	public String getStatus() {
		return status;
	}

	
}
