package org.mifosplatform.billing.pricing.serialization;

import java.util.List;
import java.util.Map;

import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.pricing.domain.Price;
import org.mifosplatform.billing.pricing.domain.PriceRepository;
import org.mifosplatform.billing.pricing.exceptions.ChargeCOdeExists;
import org.mifosplatform.billing.pricing.service.PriceReadPlatformService;
import org.mifosplatform.billing.pricing.service.PriceWritePlatformService;
import org.mifosplatform.billing.servicemaster.domain.ServiceMasterRepository;
import org.mifosplatform.billing.servicemaster.serialization.PriceCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.servicemaster.service.ServiceMasterWritePlatformServiceImpl;
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
public class PriceWritePlatformServiceImpl implements PriceWritePlatformService {

	 private final static Logger logger = LoggerFactory.getLogger(ServiceMasterWritePlatformServiceImpl.class);
		private final PlatformSecurityContext context;
		private final PriceReadPlatformService priceReadPlatformService;
		 private final PriceCommandFromApiJsonDeserializer fromApiJsonDeserializer;
		 private final PriceRepository priceRepository; 
	@Autowired
	 public PriceWritePlatformServiceImpl(final PlatformSecurityContext context,
			 final ServiceMasterRepository serviceMasterRepository,final PriceReadPlatformService priceReadPlatformService,
			 final PriceCommandFromApiJsonDeserializer fromApiJsonDeserializer,final PriceRepository priceRepository)
	{
		this.context=context;
		this.priceReadPlatformService=priceReadPlatformService;
		 this.fromApiJsonDeserializer=fromApiJsonDeserializer;
		 this.priceRepository=priceRepository;
	}
	@Override
	public CommandProcessingResult createPricing(Long planId,
			JsonCommand command) {
		
		try{
		context.authenticatedUser();
		   this.fromApiJsonDeserializer.validateForCreate(command.json());
		   List<ServiceData> serviceData = this.priceReadPlatformService.retrieveServiceCodeDetails(planId);
			
				 final Price price =Price.fromJson(command,serviceData,planId);
					for (ServiceData data : serviceData) {

						if (data.getChargeCode() != null) {
							if ((data.getPlanId() == planId) && data.getServiceCode().equalsIgnoreCase(price.getServiceCode())
									&& (data.getPriceregion().equalsIgnoreCase(price.getPriceRegion().toString())) && data.getChargeCode().equalsIgnoreCase(price.getChargeCode()))
								 {

								throw new ChargeCOdeExists(data.getChargeDescription());
							}
						}
					}
					
				
		this.priceRepository.save(price);

		return new CommandProcessingResult(price.getId());

	} catch (DataIntegrityViolationException dve) {
		 handleCodeDataIntegrityIssues(command, dve);
		return  CommandProcessingResult.empty();
	}
}

	private void handleCodeDataIntegrityIssues(JsonCommand command,
			DataIntegrityViolationException dve) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public CommandProcessingResult updatePrice(Long priceId, JsonCommand command) {
		 context.authenticatedUser();
         try{
         this.fromApiJsonDeserializer.validateForCreate(command.json());
         final Price price = retrievePriceBy(priceId);
         final  Map<String, Object> changes = price.update(command);
  if (!changes.isEmpty()) {
      this.priceRepository.save(price);
  }
  return new CommandProcessingResultBuilder() //
  .withCommandId(command.commandId()) //
  .withEntityId(priceId) //
  .with(changes) //
  .build();

         } catch (DataIntegrityViolationException dve) {
    		 handleCodeDataIntegrityIssues(command, dve);
    		return  CommandProcessingResult.empty();
    	}
	
	
}
	private Price retrievePriceBy(Long priceId) {
		Price price=this.priceRepository.findOne(priceId);
		if(price==null){
			{ throw new CodeNotFoundException(priceId.toString()); }
		}
		return price;
	}
	@Override
	public CommandProcessingResult deletePrice(Long priceId) {
		  try {
				 Price price=this.priceRepository.findOne(priceId);
				 if(price!= null){
				  price.delete();
				 }
			     this.priceRepository.save(price);
			     return new CommandProcessingResultBuilder().withEntityId(priceId).build();
				  } catch (DataIntegrityViolationException dve) {
				  }
		  return new CommandProcessingResultBuilder().withEntityId(new Long(-1)).build();
		  }
}
