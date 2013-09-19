package org.mifosplatform.billing.ownedhardware.service;

import java.util.List;

import org.mifosplatform.billing.batchjob.serialization.BatchJobFromApiJsonDeserializer;
import org.mifosplatform.billing.batchjob.service.BatchJobWritePlatformServiceImp;
import org.mifosplatform.billing.inventory.service.InventoryItemDetailsReadPlatformService;
import org.mifosplatform.billing.ownedhardware.data.OwnedHardware;
import org.mifosplatform.billing.ownedhardware.data.OwnedHardwareData;
import org.mifosplatform.billing.ownedhardware.domain.OwnedHardwareJpaRepository;
import org.mifosplatform.billing.ownedhardware.serialization.OwnedHardwareFromApiJsonDeserializer;
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
public class OwnedHardwareWritePlatformServiceImp implements OwnedHardwareWritePlatformService{

	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(OwnedHardwareWritePlatformServiceImp.class);
	
	private final OwnedHardwareJpaRepository ownedHardwareJpaRepository;
	private final PlatformSecurityContext context;
	private final OwnedHardwareFromApiJsonDeserializer apiJsonDeserializer;
	private final OwnedHardwareReadPlatformService ownedHardwareReadPlatformService;
	private final InventoryItemDetailsReadPlatformService inventoryItemDetailsReadPlatformService;
	
	
	@Autowired
	public OwnedHardwareWritePlatformServiceImp(final OwnedHardwareJpaRepository ownedHardwareJpaRepository, final PlatformSecurityContext context, final OwnedHardwareFromApiJsonDeserializer apiJsonDeserializer,
			final OwnedHardwareReadPlatformService ownedHardwareReadPlatformService, final InventoryItemDetailsReadPlatformService inventoryItemDetailsReadPlatformService) {
		this.ownedHardwareJpaRepository = ownedHardwareJpaRepository;
		this.context = context;
		this.apiJsonDeserializer = apiJsonDeserializer;
		this.ownedHardwareReadPlatformService = ownedHardwareReadPlatformService;
		this.inventoryItemDetailsReadPlatformService = inventoryItemDetailsReadPlatformService;
	}
	
	
	@Transactional
	@Override
	public CommandProcessingResult createOwnedHardware(JsonCommand command, final Long clientId) {
	OwnedHardware ownedHardware = null;
	try{	
		this.context.authenticatedUser();
		this.apiJsonDeserializer.validateForCreate(command.json());
		ownedHardware = OwnedHardware.fromJson(command,clientId);
		List<String> inventorySerialNumbers = inventoryItemDetailsReadPlatformService.retriveSerialNumbers();
		List<String> ownedhardwareSerialNumbers = ownedHardwareReadPlatformService.retriveSerialNumbers();
		String ownedHardwareSerialNumber = ownedHardware.getSerialNumber();
		if(inventorySerialNumbers.contains(ownedHardwareSerialNumber) | ownedhardwareSerialNumbers.contains(ownedHardwareSerialNumber)){
			throw new PlatformDataIntegrityException("validation.error.msg.ownedhardware.duplicate.serialNumber","validation.error.msg.ownedhardware.duplicate.serialNumber","serialNumber","");
		}
		
		this.ownedHardwareJpaRepository.save(ownedHardware);
		return new CommandProcessingResultBuilder().withEntityId(ownedHardware.getId()).build();
	}catch(DataIntegrityViolationException dve){
		handleDataIntegrityIssues(command, dve);
		return CommandProcessingResult.empty();
	}
		
	}
	
	
	@Override
	public List<OwnedHardwareData> getOwnedHardware() {

		return null;
	}
	
	
	private void handleDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {

        Throwable realCause = dve.getMostSpecificCause();
        if (realCause.getMessage().contains("serialNumber")) {
            final String name = command.stringValueOfParameterNamed("serialNumber");
            throw new PlatformDataIntegrityException("error.msg.serialnumber.duplicate.name", "serialnumber with name `" + name + "` already exists",
                    "serialnumber", name);
        }

        logger.error(dve.getMessage(), dve);
        throw new PlatformDataIntegrityException("error.msg.charge.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource: " + realCause.getMessage());
    }
}
