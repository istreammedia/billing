package org.mifosplatform.billing.address.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mifosplatform.billing.address.data.AddressData;
import org.mifosplatform.billing.address.domain.Address;
import org.mifosplatform.billing.address.domain.AddressRepository;
import org.mifosplatform.infrastructure.codes.exception.CodeNotFoundException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class AddressWritePlatformServiceImpl implements AddressWritePlatformService {
	  private final static Logger logger = LoggerFactory.getLogger(AddressWritePlatformServiceImpl.class);
	private final PlatformSecurityContext context;
	private final AddressRepository addressRepository;
	private final CityRepository cityRepository;
	private final StateRepository stateRepository;
	private final CountryRepository countryRepository;
	private final AddressReadPlatformService addressReadPlatformService;
	public static final String ADDRESSTYPE="addressType";
	
	


	@Autowired
	public AddressWritePlatformServiceImpl(final PlatformSecurityContext context,final CityRepository cityRepository,
			final AddressReadPlatformService addressReadPlatformService,final StateRepository stateRepository,
			final CountryRepository countryRepository,final AddressRepository addressRepository) {
		this.context = context;
		this.addressRepository = addressRepository;
		this.cityRepository=cityRepository;
		this.stateRepository=stateRepository;
		this.countryRepository=countryRepository;
		this.addressReadPlatformService=addressReadPlatformService;
		

	}

	@Override
	public CommandProcessingResult createAddress(Long clientId,JsonCommand command) {
		try {
			context.authenticatedUser();
			final Address address = Address.fromJson(clientId,command);
			this.addressRepository.save(address);
			return new CommandProcessingResult(address.getId());
		} catch (DataIntegrityViolationException dve) {
			 handleCodeDataIntegrityIssues(command, dve);
			return  CommandProcessingResult.empty();
		}
	}

	private void handleCodeDataIntegrityIssues(JsonCommand command,
			DataIntegrityViolationException dve) {
		  logger.error(dve.getMessage(), dve);
		
	}

	@Override
	public CommandProcessingResult updateAddress(Long addrId,JsonCommand command) {
		try
		{
			  context.authenticatedUser();
	            //this.fromApiJsonDeserializer.validateForUpdate(command.json());
	          
	            
	             Map<String, Object> changes =new HashMap();
	             List<AddressData> addressDatas =this.addressReadPlatformService.retrieveClientAddressDetails(command.longValueOfParameterNamed("clientId"));
	                
	             final String addressType=command.stringValueOfParameterNamed(ADDRESSTYPE);
	             
	             
	             if(addressDatas.size()==1 && addressType.equalsIgnoreCase("BILLING")){
	            	 
	            	 Address  newAddress=Address.fromJson(command.longValueOfParameterNamed("clientId"), command);
               	  this.addressRepository.save(newAddress);
	            	 
	             }
	                     for(AddressData addressData:addressDatas){
	                    	 
	                    	  if(addressData.getAddressType().equalsIgnoreCase(addressType))
	                    	  {
	                    		  final Address address = retrieveAddressBy(addressData.getAddressId());  
	                    		  changes = address.update(command);
	                              
	                                  this.addressRepository.save(address);
	                    	  }
         }
	                     /*for(AddressData addressData:addressDatas){
	                    	 
	                     if(!addressData.getAddressType().equalsIgnoreCase("BILLING") && addressType.equalsIgnoreCase("BILLING") ){
	                    	 
	                    	 if(addressData.getAddressType().equalsIgnoreCase("PRIMARY")){
	                    		 
	                    	 }
	                    	 else{
	                    	 Address  newAddress=Address.fromJson(command.longValueOfParameterNamed("clientId"), command);
	                    	  this.addressRepository.save(newAddress);
	                    	 }
	                     }*/
	                   //  }
         return new CommandProcessingResultBuilder() //
         .withCommandId(command.commandId()) //
         .withEntityId(addrId) //
         .with(changes) //
         .build();
	} catch (DataIntegrityViolationException dve) {
		 handleCodeDataIntegrityIssues(command, dve);
		return new CommandProcessingResult(Long.valueOf(-1));
	}
}

	private Address retrieveAddressBy(Long addrId) {
		Address address=this.addressRepository.findOne(addrId);
	    if(address== null){
		throw new CodeNotFoundException(addrId);
	    }
	return address;
	}

	@Override
	public CommandProcessingResult createNewRecord(JsonCommand command,String entityType) {
  try{
	  
	  this.context.authenticatedUser();
	 
	  if(entityType.equalsIgnoreCase("city")){
		//  City city=new City(command.getEntityCode(),command.getEntityName(),command.getParentEntityId());
			final City city = City.fromJson(command);
		  this.cityRepository.save(city);
		  return new CommandProcessingResult(Long.valueOf(city.getId()));
	  }else if(entityType.equalsIgnoreCase("state")){
		  
		  State state=State.fromJson(command);
		  this.stateRepository.save(state);
		  
		  return new CommandProcessingResult(Long.valueOf(state.getId()));
	  }else{
		  
		  Country country=Country.fromJson(command);
		  this.countryRepository.save(country);
		  return new CommandProcessingResult(Long.valueOf(country.getId()));
	  }
	  
		  
	  
  } catch (DataIntegrityViolationException dve) {
	  handleCodeDataIntegrityIssues(command, dve);
		return new CommandProcessingResult(Long.valueOf(-1));
	}

	}
}

