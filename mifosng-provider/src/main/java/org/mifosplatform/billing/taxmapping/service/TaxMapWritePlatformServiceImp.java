package org.mifosplatform.billing.taxmapping.service;

import java.util.Map;

import org.mifosplatform.billing.taxmapping.domain.TaxMap;
import org.mifosplatform.billing.taxmapping.domain.TaxMapRepository;
import org.mifosplatform.billing.taxmapping.serialization.TaxMapCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaxMapWritePlatformServiceImp implements TaxMapWritePlatformService{

	private final static Logger logger = (Logger) LoggerFactory.getLogger(TaxMapWritePlatformService.class);
	
	private final PlatformSecurityContext context;
	private final TaxMapRepository taxMapRepository;
	private final TaxMapCommandFromApiJsonDeserializer taxMapCommandFromApiJsonDeserializer;
	
	@Autowired
	public TaxMapWritePlatformServiceImp(final PlatformSecurityContext context,final TaxMapRepository taxMapRepository,final TaxMapCommandFromApiJsonDeserializer taxMapCommandFromApiJsonDeserializer){
		this.context = context;
		this.taxMapRepository = taxMapRepository;
		this.taxMapCommandFromApiJsonDeserializer = taxMapCommandFromApiJsonDeserializer;
		
	}
	
	@Override
	@Transactional
	public CommandProcessingResult createTaxMap(JsonCommand command){
		TaxMap entity = null;
		try{
			context.authenticatedUser();
			taxMapCommandFromApiJsonDeserializer.validateForCreate(command);
			entity = TaxMap.fromJson(command);
			taxMapRepository.save(entity);
		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command, dve);
			//throw new PlatformDataIntegrityException(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
			return new CommandProcessingResult(Long.valueOf(-1));
		}
		return new CommandProcessingResultBuilder().withEntityId(entity.getId()).build();
	}
	
	 private TaxMap retrieveTaxMapBy(final Long taxMapId) {
	        final TaxMap taxMap = this.taxMapRepository.findOne(taxMapId);
	        if (taxMap == null) { throw new PlatformDataIntegrityException("validation.error.msg.taxmap.taxcode.doesnotexist","validation.error.msg.taxmap.taxcode.doesnotexist",taxMapId.toString(),"validation.error.msg.taxmap.taxcode.doesnotexist"); }
	        return taxMap;
	    }
	
	
	@Override
	@Transactional
	public CommandProcessingResult updateTaxMap(final JsonCommand command,final Long taxMapId){
		TaxMap taxMap = null;
		try{
			context.authenticatedUser();
			taxMapCommandFromApiJsonDeserializer.validateForCreate(command);
			taxMap = retrieveTaxMapBy(taxMapId);
			final Map<String, Object> changes = taxMap.update(command);
			if(!changes.isEmpty()){
				taxMapRepository.save(taxMap);
			}
			
			return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(command.entityId()).with(changes).build();
		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command, dve);
			return CommandProcessingResult.empty();
		}
	
	}
	
	
	private void handleDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {

        Throwable realCause = dve.getMostSpecificCause();
       if (realCause.getMessage().contains("taxcode")){
       	throw new PlatformDataIntegrityException("validation.error.msg.taxmap.taxcode.duplicate", "validation.error.msg.taxmap.taxcode.duplicate", "validation.error.msg.taxmap.taxcode.duplicate","");
       	
       }


       logger.error(dve.getMessage(), dve);   	
}
}
