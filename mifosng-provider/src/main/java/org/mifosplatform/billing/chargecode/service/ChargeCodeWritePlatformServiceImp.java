package org.mifosplatform.billing.chargecode.service;

import java.util.Map;

import org.mifosplatform.billing.chargecode.domain.ChargeCode;
import org.mifosplatform.billing.chargecode.domain.ChargeCodeRepository;
import org.mifosplatform.billing.chargecode.serialization.ChargeCodeCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.codes.exception.CodeNotFoundException;
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
public class ChargeCodeWritePlatformServiceImp implements ChargeCodeWritePlatformService{

	private final static Logger logger = (Logger) LoggerFactory.getLogger(ChargeCodeWritePlatformServiceImp.class);	
	
	private PlatformSecurityContext platformSecurityContext;
	private ChargeCodeRepository chargeCodeRepository; 
	private ChargeCodeCommandFromApiJsonDeserializer chargeCodeCommandFromApiJsonDeserializer;
	private ChargeCodeReadPlatformService chargeCodeReadPlatformService; 
	
	@Autowired
	public ChargeCodeWritePlatformServiceImp(final PlatformSecurityContext platformSecurityContext, final ChargeCodeRepository chargeCodeRepository, final ChargeCodeCommandFromApiJsonDeserializer chargeCodeCommandFromApiJsonDeserializer,final ChargeCodeReadPlatformService chargeCodeReadPlatformService) {
		this.platformSecurityContext = platformSecurityContext;
		this.chargeCodeRepository = chargeCodeRepository;
		this.chargeCodeCommandFromApiJsonDeserializer = chargeCodeCommandFromApiJsonDeserializer;
		this.chargeCodeReadPlatformService = chargeCodeReadPlatformService;
	}
	
	@Transactional
	@Override
	public CommandProcessingResult createChargeCode(JsonCommand command) {
		ChargeCode chargeCode = null;
		try{
			platformSecurityContext.authenticatedUser();
			chargeCodeCommandFromApiJsonDeserializer.validaForCreate(command.json());
			chargeCode = ChargeCode.fromJson(command);
			chargeCodeRepository.save(chargeCode);
			return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(chargeCode.getId()).build();
		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command, dve);
			return CommandProcessingResult.empty();
		}
	}
	
	private void handleDataIntegrityIssues(JsonCommand command, DataIntegrityViolationException dve){
		Throwable realCause = dve.getMostSpecificCause(); 
		if(realCause.getMessage().contains("chargecode")){
			throw new PlatformDataIntegrityException("validation.error.message.chargecode.duplicate.chargecode","","",command.stringValueOfParameterNamed("chargecode"));
		}
		logger.error(dve.getMessage(), dve);
	}

	   private ChargeCode retrieveCodeBy(final Long codeId) {
	        final ChargeCode code = this.chargeCodeRepository.findOne(codeId);
	        if (code == null) { throw new CodeNotFoundException(codeId.toString()); }
	        return code;
	    }
	
	@Transactional
	@Override
	public CommandProcessingResult updateChargeCode(JsonCommand command,Long chargeCodeId) {
		ChargeCode chargeCode = null;
		try{
			platformSecurityContext.authenticatedUser();
			this.chargeCodeCommandFromApiJsonDeserializer.validaForCreate(command.json());
			chargeCode = retrieveCodeBy(chargeCodeId);
			final Map<String, Object> changes = chargeCode.update(command);
			if(!changes.isEmpty()){
				chargeCodeRepository.save(chargeCode);
			}
			
			return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(chargeCode.getId()).with(changes).build();
		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command,dve);
			return CommandProcessingResult.empty();
		}
	}



}
