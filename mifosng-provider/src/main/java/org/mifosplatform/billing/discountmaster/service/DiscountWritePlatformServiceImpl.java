package org.mifosplatform.billing.discountmaster.service;

import java.util.Map;

import org.mifosplatform.billing.discountmaster.domain.DiscountMaster;
import org.mifosplatform.billing.discountmaster.domain.DiscountMasterRepository;
import org.mifosplatform.billing.discountmaster.serialization.DiscountCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.codes.exception.DiscountNotFoundException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class DiscountWritePlatformServiceImpl implements DiscountWritePlatformService{
	
	private final static Logger logger = LoggerFactory.getLogger(DiscountWritePlatformServiceImpl.class);
    private final PlatformSecurityContext context;
	private final DiscountCommandFromApiJsonDeserializer apiJsonDeserializer;
	private final DiscountMasterRepository discountMasterRepository;
	
	
@Autowired	
public DiscountWritePlatformServiceImpl(final PlatformSecurityContext context,DiscountCommandFromApiJsonDeserializer apiJsonDeserializer,
		final DiscountMasterRepository discountMasterRepository)
{
	this.context=context;
	this.apiJsonDeserializer=apiJsonDeserializer;
	this.discountMasterRepository=discountMasterRepository;
}

	@Override
	public CommandProcessingResult createNewDiscount(JsonCommand command) {
       
		try{
			
			this.context.authenticatedUser();
			this.apiJsonDeserializer.validateForCreate(command.json());
			DiscountMaster discountMaster=DiscountMaster.fromJson(command);
			this.discountMasterRepository.save(discountMaster);
			return new CommandProcessingResult(discountMaster.getId());
			
			
		}catch(DataIntegrityViolationException dve){
			handleCodeDataIntegrityIssues(command, dve);
			return null;
		}
	
	}

	
	private void handleCodeDataIntegrityIssues(JsonCommand command,
			DataIntegrityViolationException dve) {
		 Throwable realCause = dve.getMostSpecificCause();
	        if (realCause.getMessage().contains("discountcode")) {
	            final String name = command.stringValueOfParameterNamed("discountcode");
	            throw new PlatformDataIntegrityException("error.msg.discount.duplicate.name", "A discount with Code'"
	                    + name + "'already exists", "displayName", name);
	        }

	        logger.error(dve.getMessage(), dve);
	        throw new PlatformDataIntegrityException("error.msg.cund.unknown.data.integrity.issue",
	                "Unknown data integrity issue with resource: " + realCause.getMessage());
		
	}

	@Override
	public CommandProcessingResult updateDiscount(Long entityId,JsonCommand command)
	{
        try{
        	  
        	this.context.authenticatedUser();
        	this.apiJsonDeserializer.validateForCreate(command.json());
        	
        	DiscountMaster discountMaster=DiscountretrieveById(entityId);
        	final Map<String, Object> changes = discountMaster.update(command);  
        	
        	if(!changes.isEmpty()){
        		this.discountMasterRepository.save(discountMaster);
        	}
        	
        	return new CommandProcessingResult(entityId);
        	
        }catch(DataIntegrityViolationException dve){
        	handleCodeDataIntegrityIssues(command, dve);
        	return null;        }
		
         
}

	private DiscountMaster DiscountretrieveById(Long entityId) {
             
              DiscountMaster discountMaster=this.discountMasterRepository.findOne(entityId);
              if (discountMaster== null) { throw new DiscountNotFoundException(entityId.toString()); }
	          return discountMaster;	
	}

	@Override
	public CommandProcessingResult deleteDiscount(Long entityId) {

     try{
    	 this.context.authenticatedUser();
    	 DiscountMaster discountMaster=this.discountMasterRepository.findOne(entityId);
    	 
    	 if(discountMaster==null){
    		 throw new DiscountNotFoundException(entityId.toString());
    	 }
    	 
    	 discountMaster.delete();
    	 this.discountMasterRepository.save(discountMaster);
    	 return new CommandProcessingResult(entityId);
    	 
    	 
     }catch(Exception exception){
    	 return null;
     }
	}
}
