package org.mifosplatform.billing.region.service;

import java.util.List;
import java.util.Map;

import org.mifosplatform.accounting.autoposting.exception.RegionDuplicateException;
import org.mifosplatform.billing.epgprogramguide.serialization.RegionFromApiJsonDeserializer;
import org.mifosplatform.billing.inventory.service.InventoryItemDetailsWritePlatformServiceImp;
import org.mifosplatform.billing.order.exceptions.CountryRegionDuplicateException;
import org.mifosplatform.billing.order.exceptions.NoOrdersFoundException;
import org.mifosplatform.billing.region.data.RegionDetailsData;
import org.mifosplatform.billing.region.domain.RegionDetails;
import org.mifosplatform.billing.region.domain.RegionJpaRepository;
import org.mifosplatform.billing.region.domain.RegionMaster;
import org.mifosplatform.billing.region.exception.RegionNotFoundException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;



@Service
public class RegionWriteplatformServiceImpl implements RegionWriteplatformService{
	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(InventoryItemDetailsWritePlatformServiceImp.class);
	private final PlatformSecurityContext context;
	private final RegionJpaRepository regionJpaRepository;
	private final  RegionFromApiJsonDeserializer apiJsonDeserializer;
	private final RegionReadPlatformService  regionReadPlatformService;
	
	
	@Autowired
	public RegionWriteplatformServiceImpl(final PlatformSecurityContext context, final RegionJpaRepository regionJpaRepository,
			final RegionFromApiJsonDeserializer jsonDeserializer,final RegionReadPlatformService regionReadPlatformService) {
		
		this.context = context;
		this.regionJpaRepository = regionJpaRepository;
		this.apiJsonDeserializer = jsonDeserializer;
		this.regionReadPlatformService=regionReadPlatformService;
	}


	@Override
	public CommandProcessingResult createNewRegion(JsonCommand command) {
		
		try{
			 this.context.authenticatedUser();
		     this.apiJsonDeserializer.validateForCreate(command.json());
		     
		     
			  RegionMaster regionMaster=RegionMaster.fromJson(command);
			 final JsonArray array=command.arrayOfParameterNamed("states").getAsJsonArray();
			 
			    String[] states =null;
			    states=new String[array.size()];
			    for(int i=0; i<array.size();i++){
			    	states[i] =array.get(i).getAsString();
			    }
			   
				 for (String id : states) {
					 
					 final Long countryId = command.longValueOfParameterNamed("countryId");
		                final Long stateId = Long.valueOf(id);
		                List<RegionDetailsData> detailsDatas=this.regionReadPlatformService.getCountryRegionDetails(countryId, stateId);
		                if(!detailsDatas.isEmpty()){
		                	throw new CountryRegionDuplicateException();
		                }
		                RegionDetails detail=new RegionDetails(countryId,stateId);
		                regionMaster.addRegionDetails(detail);
				  }
         this.regionJpaRepository.save(regionMaster);
         
        return new CommandProcessingResult(regionMaster.getId()); 	
			
		}catch (final DataIntegrityViolationException dve) {
            handleRegionDataIntegrityIssues(command, dve);
            return CommandProcessingResult.empty();
        }
		
	}


	private void handleRegionDataIntegrityIssues(JsonCommand command,DataIntegrityViolationException dve) {

        final Throwable realCause = dve.getMostSpecificCause();
        if (realCause.getMessage().contains("priceregion_code_key")) { 
        	throw new RegionDuplicateException(command.stringValueOfParameterNamed("regionCode"));
        	}else if(realCause.getMessage().contains("state_id_key")) {
        		
        		throw new RegionDuplicateException(command.longValueOfParameterNamed("countryId"));
        		
        	}

        logger.error(dve.getMessage(), dve);
        throw new PlatformDataIntegrityException("error.msg.region.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource re: " + realCause.getMessage());
    
		
	}


	@Override
	public CommandProcessingResult updateRegion(JsonCommand command) {
		try{
			
			context.authenticatedUser();
            this.apiJsonDeserializer.validateForCreate(command.json());
            final RegionMaster regionMaster=retrieveRegionMasterById(command.entityId());
            
          //  List<RegionDetails> details=regionMaster.getDetails();
            regionMaster.getDetails().clear();
           
 				
 				final JsonArray array=command.arrayOfParameterNamed("states").getAsJsonArray();
			    String[] states =null;
			    states=new String[array.size()];
			    for(int i=0; i<array.size();i++){
			    	states[i] =array.get(i).getAsString();
			    }
			           
				 for (String stateId : states) {
					  
		                final Long id = Long.valueOf(stateId);
		                final Long countryId = command.longValueOfParameterNamed("countryId");
		                RegionDetails regionDetails=new RegionDetails(countryId, id);
		                regionMaster.addRegionDetails(regionDetails);
				  }
 				
 			
 		  final Map<String, Object> changes = regionMaster.update(command);
 		  
 		  if(!changes.isEmpty())
             this.regionJpaRepository.save(regionMaster);
             
        return new CommandProcessingResult(regionMaster.getId());    
			
		}catch (final DataIntegrityViolationException dve) {
            handleRegionDataIntegrityIssues(command, dve);
            return CommandProcessingResult.empty();
        }
		
		
	}


	private RegionMaster retrieveRegionMasterById(Long entityId) {
		

		 final RegionMaster regionMaster= this.regionJpaRepository.findOne(entityId);
	        if (regionMaster == null) { throw new RegionNotFoundException(entityId.toString()); }
	        return regionMaster;

}


	@Override
	public CommandProcessingResult deleteRegion(Long entityId) {
		try{
                  
			RegionMaster regionMaster=this.regionJpaRepository.findOne(entityId);
			
			regionMaster.delete();
		 
		 this.regionJpaRepository.save(regionMaster);
		 
		  return new CommandProcessingResultBuilder().withEntityId(entityId).build();
	}catch (final DataIntegrityViolationException dve) {
        handleRegionDataIntegrityIssues(null, dve);
        return CommandProcessingResult.empty();
	}
	}
}
