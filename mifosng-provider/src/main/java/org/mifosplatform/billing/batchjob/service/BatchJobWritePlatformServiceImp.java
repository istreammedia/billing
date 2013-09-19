/**
 * 
 */
package org.mifosplatform.billing.batchjob.service;

import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.mifosplatform.billing.batchjob.domain.BatchJob;
import org.mifosplatform.billing.batchjob.domain.BatchJobJpaRepository;
import org.mifosplatform.billing.batchjob.serialization.BatchJobFromApiJsonDeserializer;
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

/**
 * @author Praveen
 *
 */

@Service
public class BatchJobWritePlatformServiceImp implements BatchJobWritePlatformService {

	private final static Logger logger = (Logger) LoggerFactory.getLogger(BatchJobWritePlatformServiceImp.class);
	
	private BatchJobJpaRepository jpaRepository;
	private PlatformSecurityContext context;
	private BatchJobFromApiJsonDeserializer apiJsonDeserializer;
	
	@Autowired
	public BatchJobWritePlatformServiceImp(final BatchJobJpaRepository jpaRepository, final PlatformSecurityContext context, final BatchJobFromApiJsonDeserializer apiJsonDeserializer) {
		this.context = context;
		this.jpaRepository = jpaRepository;
		this.apiJsonDeserializer = apiJsonDeserializer;
	}
	
	
	@Override
	@Transactional
	public CommandProcessingResult creatBatch(JsonCommand command) {
		BatchJob batch = null;
		try{
			context.authenticatedUser();
			this.apiJsonDeserializer.validateForCreate(command.json());
			batch = BatchJob.fromJson(command);
			jpaRepository.save(batch);
		return new CommandProcessingResultBuilder().withEntityId(batch.getId()).build();
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
