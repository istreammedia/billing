package org.mifosplatform.billing.batchjobscheduling.service;

import org.mifosplatform.billing.batchjobscheduling.domain.BatchJobScheduling;
import org.mifosplatform.billing.batchjobscheduling.domain.BatchJobSchedulingJpaRepository;
import org.mifosplatform.billing.batchjobscheduling.serialization.BatchJobSchedulingCommandFromApiJsonDeserializer;
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

@Service
public class BatchJobSchedulingWritePlatformServiceImp implements BatchJobSchedulingWritePlatformService {

	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(BatchJobSchedulingWritePlatformServiceImp.class);
	private PlatformSecurityContext context;
	private BatchJobSchedulingJpaRepository jpaRepository;
	private BatchJobSchedulingCommandFromApiJsonDeserializer batchJobSchedulingCommandFromApiJsonDeserializer; 
	
	@Autowired
	public BatchJobSchedulingWritePlatformServiceImp(final PlatformSecurityContext context, final BatchJobSchedulingJpaRepository jpaRepository, final BatchJobSchedulingCommandFromApiJsonDeserializer batchJobSchedulingCommandFromApiJsonDeserializer) {
		this.context = context;
		this.jpaRepository = jpaRepository;
		this.batchJobSchedulingCommandFromApiJsonDeserializer = batchJobSchedulingCommandFromApiJsonDeserializer;
	}
	
	@Override
	public CommandProcessingResult createJobSchedule(JsonCommand command) {
		BatchJobScheduling batchJobScheduling = null;
		try{
			
			context.authenticatedUser();
			batchJobSchedulingCommandFromApiJsonDeserializer.validateForCreate(command.json());	
			batchJobScheduling = BatchJobScheduling.fromJson(command);
			jpaRepository.save(batchJobScheduling);
			return new CommandProcessingResultBuilder().withEntityId(batchJobScheduling.getId()).build();
		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command, dve);
			return CommandProcessingResult.empty();
		}
		
	}
	
	private void handleDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {

        Throwable realCause = dve.getMostSpecificCause();
        if (realCause.getMessage().contains("batchName")) {
            final String name = command.stringValueOfParameterNamed("batchName");
            throw new PlatformDataIntegrityException("error.msg.batch.duplicate.name", "Batch with name `" + name + "` already exists",
                    "batchName", name);
        }

        logger.error(dve.getMessage(), dve);
        throw new PlatformDataIntegrityException("error.msg.charge.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource: " + realCause.getMessage());
    }
}
