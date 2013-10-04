package org.mifosplatform.billing.contract.service;

import java.util.Map;

import org.mifosplatform.billing.contract.domain.Contract;
import org.mifosplatform.billing.contract.domain.SubscriptionRepository;
import org.mifosplatform.billing.contract.serialization.ContractCommandFromApiJsonDeserializer;
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
public class ContractPeriodWritePlatformServiceImp implements ContractPeriodWritePlatformService{

	private final PlatformSecurityContext context;
	 private final ContractCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	 public ContractPeriodWritePlatformServiceImp(final PlatformSecurityContext context,
			 final SubscriptionRepository subscriptionRepository,final ContractCommandFromApiJsonDeserializer fromApiJsonDeserializer)
	{
		this.context=context;
		this.subscriptionRepository=subscriptionRepository;
		 this.fromApiJsonDeserializer=fromApiJsonDeserializer;
	}

	@Override
	public CommandProcessingResult createContract(JsonCommand command) {
		try
		{
		context.authenticatedUser();
		   this.fromApiJsonDeserializer.validateForCreate(command.json());
		final Contract contract = Contract.fromJson(command);
		this.subscriptionRepository.save(contract);
			return new CommandProcessingResult(contract.getId());

	} catch (DataIntegrityViolationException dve) {
		 handleCodeDataIntegrityIssues(command, dve);
		return  CommandProcessingResult.empty();
	}
}
		private void handleCodeDataIntegrityIssues(JsonCommand command,
			DataIntegrityViolationException dve) {

	        Throwable realCause = dve.getMostSpecificCause();
	        if (realCause.getMessage().contains("contract_period_key")) {
	            final String name = command.stringValueOfParameterNamed("subscriptionPeriod");
	            throw new PlatformDataIntegrityException("error.msg.code.duplicate.name", "A code with name '" + name + "' already exists",name);
	        }

	        
	        throw new PlatformDataIntegrityException("error.msg.cund.unknown.data.integrity.issue",
	                "Unknown data integrity issue with resource: " + realCause.getMessage());
	    
		
	}

		@Override
		public CommandProcessingResult updateContract(Long contractId,
				JsonCommand command) {
			try
			{
				  context.authenticatedUser();
		            this.fromApiJsonDeserializer.validateForCreate(command.json());
		            final Contract contract = retrieveCodeBy(contractId);

	         final Map<String, Object> changes = contract.update(command);

	         if (!changes.isEmpty()) {
	             this.subscriptionRepository.save(contract);
	         }
	         return new CommandProcessingResultBuilder() //
	         .withCommandId(command.commandId()) //
	         .withEntityId(contractId) //
	         .with(changes) //
	         .build();

			
		} catch (DataIntegrityViolationException dve) {
			 handleCodeDataIntegrityIssues(command, dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}
		
		}

		private Contract retrieveCodeBy(final Long contractId) {
	        final Contract contract = this.subscriptionRepository.findOne(contractId);
	        if (contract == null) { throw new CodeNotFoundException(contractId.toString()); }
	        return contract;
	    }

		@Override
		public CommandProcessingResult deleteContract(Long contractId) {
			   context.authenticatedUser();

		        final Contract contract = retrieveCodeBy(contractId);

		        this.subscriptionRepository.delete(contract);

		        return new CommandProcessingResultBuilder().withEntityId(contractId).build();
		    }

		
		





}
	
