package org.mifosplatform.billing.selfcare.service;

import java.util.Date;

import org.mifosplatform.billing.selfcare.domain.SelfCare;
import org.mifosplatform.billing.transactionhistory.service.TransactionHistoryWritePlatformService;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.domain.EmailDetail;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.core.service.PlatformEmailService;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.infrastructure.security.service.RandomPasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


@Service
public class SelfCareWritePlatformServiceImp implements SelfCareWritePlatformService{
	
	private PlatformSecurityContext context;
	private SelfCareRepository selfCareRepository;
	private FromJsonHelper fromJsonHelper;
	private SelfCareCommandFromApiJsonDeserializer selfCareCommandFromApiJsonDeserializer;
	private SelfCareReadPlatformService selfCareReadPlatformService;
	private PlatformEmailService platformEmailService; 
	private TransactionHistoryWritePlatformService transactionHistoryWritePlatformService;
	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(SelfCareWritePlatformServiceImp.class);
	
	@Autowired
	public SelfCareWritePlatformServiceImp(final PlatformSecurityContext context, final SelfCareRepository selfCareRepository, final FromJsonHelper fromJsonHelper, final SelfCareCommandFromApiJsonDeserializer selfCareCommandFromApiJsonDeserializer, final SelfCareReadPlatformService selfCareReadPlatformService, final PlatformEmailService platformEmailService, final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService) {
		this.context = context;
		this.selfCareRepository = selfCareRepository;
		this.fromJsonHelper = fromJsonHelper;
		this.selfCareCommandFromApiJsonDeserializer = selfCareCommandFromApiJsonDeserializer;
		this.selfCareReadPlatformService = selfCareReadPlatformService;
		this.platformEmailService = platformEmailService;
		this.transactionHistoryWritePlatformService = transactionHistoryWritePlatformService;
	}
	
	@Override
	public CommandProcessingResult createSelfCare(JsonCommand command) {
		SelfCare selfCare = null;
		Long clientId = null;
		String email = null;
		try{
			context.authenticatedUser();
			selfCareCommandFromApiJsonDeserializer.validateForCreate(command);
			selfCare = SelfCare.fromJson(command);
			try{
			clientId = selfCareReadPlatformService.getClientId(selfCare.getUniqueReference());
			email = selfCareReadPlatformService.getEmail(clientId);
			}catch(EmptyResultDataAccessException erdae){
				throw new PlatformDataIntegrityException("invalid.account.details","invalid.account.details");
			}
			
			if(clientId !=null && clientId > 0 ){
				selfCare.setClientId(clientId);
				RandomPasswordGenerator passwordGenerator = new RandomPasswordGenerator(8);
				String unencodedPassword = passwordGenerator.generate();
				selfCare.setPassword(unencodedPassword);
				selfCareRepository.save(selfCare);
				
				platformEmailService.sendToUserAccount(new EmailDetail("Hugo Self Care Organisation ", "SelfCare",email, selfCare.getUserName()), unencodedPassword); 
				
				transactionHistoryWritePlatformService.saveTransactionHistory(clientId, "Self Care user activation", new Date(), "USerName: "+selfCare.getUserName()+" ClientId"+selfCare.getClientId());
			}else{
				throw new PlatformDataIntegrityException("client does not exist", "client not registered","clientId", "client is null ");
			}
			
		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command, dve);
			throw new PlatformDataIntegrityException("duplicate.username", "duplicate.username","duplicate.username", "duplicate.username");
		}catch(EmptyResultDataAccessException emp){
			throw new PlatformDataIntegrityException("empty.result.set", "empty.result.set");
		}
		
		return new CommandProcessingResultBuilder().withEntityId(selfCare.getClientId()).build();
	}
	
	
	
	
	
	private void handleDataIntegrityIssues(JsonCommand command,DataIntegrityViolationException dve) {
		 Throwable realCause = dve.getMostSpecificCause();
	        if (realCause.getMessage().contains("username")){
	        	throw new PlatformDataIntegrityException("validation.error.msg.selfcare.duplicate.userName", "User Name: " + command.stringValueOfParameterNamed("userName")+ " already exists", "userName", command.stringValueOfParameterNamed("userName"));
	        }


	        logger.error(dve.getMessage(), dve);
		
	}
	
}