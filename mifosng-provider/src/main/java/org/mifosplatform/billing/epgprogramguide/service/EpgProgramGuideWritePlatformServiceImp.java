package org.mifosplatform.billing.epgprogramguide.service;

import org.mifosplatform.billing.epgprogramguide.domain.EpgProgramGuide;
import org.mifosplatform.billing.epgprogramguide.domain.EpgProgramGuideJpaRepository;
import org.mifosplatform.billing.epgprogramguide.serialization.EpgProgramGuideFromApiJsonDeserializer;
import org.mifosplatform.billing.inventory.service.InventoryItemDetailsWritePlatformServiceImp;
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
import org.springframework.transaction.annotation.Transactional;

@Service
public class EpgProgramGuideWritePlatformServiceImp implements
		EpgProgramGuideWritePlatformService {

	private final static Logger logger = (Logger) LoggerFactory.getLogger(InventoryItemDetailsWritePlatformServiceImp.class);
	private PlatformSecurityContext context;
	private EpgProgramGuideJpaRepository epgProgramGuideJpaRepository;
	private FromJsonHelper fromJsonHelper;
	private EpgProgramGuideFromApiJsonDeserializer epgProgramGuideFromApiJsonDeserializer;
	
	@Autowired
	public EpgProgramGuideWritePlatformServiceImp(final PlatformSecurityContext context, final EpgProgramGuideJpaRepository epgProgramGuideJpaRepository, final FromJsonHelper fromJsonHelper,final EpgProgramGuideFromApiJsonDeserializer epgProgramGuideFromApiJsonDeserializer) {
		this.context = context;
		this.epgProgramGuideJpaRepository = epgProgramGuideJpaRepository;
		this.fromJsonHelper = fromJsonHelper;
		this.epgProgramGuideFromApiJsonDeserializer = epgProgramGuideFromApiJsonDeserializer;
	}
	
	@Transactional
	@Override
	public CommandProcessingResult createEpgProgramGuide(JsonCommand command) {
		EpgProgramGuide epgProgramGuide = null;
		try{
			this.context.authenticatedUser();
			epgProgramGuideFromApiJsonDeserializer.validateForCreate(command.json());
			epgProgramGuide = EpgProgramGuide.fromJson(command);
			epgProgramGuideJpaRepository.save(epgProgramGuide);

		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command, dve);
			throw new PlatformDataIntegrityException("Data not saved-1","Data not saved-2","Data not saved-3");
		}
		return new CommandProcessingResultBuilder().withCommandId(1L).withEntityId(epgProgramGuide.getId()).build();
	}
	
	private void handleDataIntegrityIssues(final JsonCommand element, final DataIntegrityViolationException dve) {

        Throwable realCause = dve.getMostSpecificCause();
       if (realCause.getMessage().contains("serial_no_constraint")){
       	throw new PlatformDataIntegrityException("validation.error.msg.inventory.item.duplicate.serialNumber", "validation.error.msg.inventory.item.duplicate.serialNumber", "validation.error.msg.inventory.item.duplicate.serialNumber","");
       	
       }


       logger.error(dve.getMessage(), dve);   	

	
	}		
}
