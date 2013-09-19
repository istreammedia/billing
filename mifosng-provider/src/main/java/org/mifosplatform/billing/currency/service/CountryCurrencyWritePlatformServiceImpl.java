package org.mifosplatform.billing.currency.service;

import java.util.List;
import java.util.Map;

import org.mifosplatform.billing.currency.data.CountryCurrencyData;
import org.mifosplatform.billing.currency.domain.CountryCurrencyRepository;
import org.mifosplatform.billing.currency.exception.DuplicateCurrencyConfigurationException;
import org.mifosplatform.billing.currency.serialization.CountryCurrencyCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.pricing.domain.CountryCurrency;
import org.mifosplatform.billing.servicemaster.domain.ServiceMaster;
import org.mifosplatform.infrastructure.codes.exception.CodeNotFoundException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CountryCurrencyWritePlatformServiceImpl implements CountryCurrencyWritePlatformService{
	private final PlatformSecurityContext context;
	private final CountryCurrencyCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final CountryCurrencyReadPlatformService countryCurrencyReadPlatformService;
	private final CountryCurrencyRepository countryCurrencyRepository;
	

@Autowired	
public CountryCurrencyWritePlatformServiceImpl(final PlatformSecurityContext context,CountryCurrencyCommandFromApiJsonDeserializer apiJsonDeserializer,
		final CountryCurrencyReadPlatformService countryCurrencyReadPlatformService,final CountryCurrencyRepository countryCurrencyRepository){
	
	this.context=context;
	this.fromApiJsonDeserializer=apiJsonDeserializer;
	this.countryCurrencyReadPlatformService=countryCurrencyReadPlatformService;
	this.countryCurrencyRepository=countryCurrencyRepository;
}
	
	@Override
	public CommandProcessingResult createCountryCurrency(JsonCommand command) {
		
		try{
			
			this.context.authenticatedUser();
			this.fromApiJsonDeserializer.validateForCreate(command.json());
			
			 final CountryCurrency countryCurrency=CountryCurrency.fromJson(command);
			
		//	List<CountryCurrencyData> currencyDatas = this.countryCurrencyReadPlatformService.getTheCountryCurrencyDetaiils(countryCurrency.getCountry());
			
			/*for(CountryCurrencyData currencyData:currencyDatas){
			     if(currencyData.getCurrency().equalsIgnoreCase(countryCurrency.getCurrency())){
			    	 throw new DuplicateCurrencyConfigurationException(countryCurrency.getCountry());
			     }
				
			}*/
			this.countryCurrencyRepository.save(countryCurrency);
			return new CommandProcessingResult(countryCurrency.getId());
			
		}catch(DataIntegrityViolationException dve){
			handleDataIntegretyIssue(dve,command);
			 return CommandProcessingResult.empty();
			
		}
		
	}

	private void handleDataIntegretyIssue(DataIntegrityViolationException dve, JsonCommand command) {

        Throwable realCause = dve.getMostSpecificCause();
        if (realCause.getMessage().contains("country_key")) {
            final String name = command.stringValueOfParameterNamed("country");
            throw new PlatformDataIntegrityException("error.msg.currency.duplicate.configuration", "Country is already configured with'"
                    + name + "'", "displayName", name);
        }

       // logger.error(dve.getMessage(), dve);
        throw new PlatformDataIntegrityException("error.msg.cund.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource: " + realCause.getMessage());
    
	    }

	@Override
	public CommandProcessingResult updateCurrencyConfig(Long entityId,
			JsonCommand command) {
		try
		{
			    context.authenticatedUser();
	            this.fromApiJsonDeserializer.validateForCreate(command.json());
	            final CountryCurrency countryCurrency = retrieveCodeBy(entityId);
                final Map<String, Object> changes = countryCurrency.update(command);
         if (!changes.isEmpty()) {
             this.countryCurrencyRepository.save(countryCurrency);
         }
         return new CommandProcessingResultBuilder() //
         .withCommandId(command.commandId()) //
         .withEntityId(entityId) //
         .with(changes) //
         .build();
	} catch (DataIntegrityViolationException dve) {
		handleDataIntegretyIssue( dve,command);
		return new CommandProcessingResult(Long.valueOf(-1));
	}
		
	}

	private CountryCurrency retrieveCodeBy(final Long serviceId) {
	        final CountryCurrency countryCurrency = this.countryCurrencyRepository.findOne(serviceId);
	        if (countryCurrency == null) { throw new CodeNotFoundException(serviceId.toString()); }
	        return countryCurrency;
	    }

	@Override
	public CommandProcessingResult deleteCountryCurrency(Long entityId) {
		
	
		 context.authenticatedUser();
	        final CountryCurrency countryCurrency = retrieveCodeBy(entityId);
	        countryCurrency.delete();
			this.countryCurrencyRepository.save(countryCurrency);
	        return new CommandProcessingResultBuilder().withEntityId(entityId).build();
}
}


