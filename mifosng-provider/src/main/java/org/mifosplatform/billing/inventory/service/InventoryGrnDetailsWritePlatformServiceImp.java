package org.mifosplatform.billing.inventory.service;

import org.mifosplatform.billing.inventory.domain.InventoryGrn;
import org.mifosplatform.billing.inventory.domain.InventoryGrnRepository;
import org.mifosplatform.billing.inventory.serialization.InventoryGrnCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryGrnDetailsWritePlatformServiceImp implements InventoryGrnDetailsWritePlatformService{

	
	private PlatformSecurityContext context;
	private InventoryGrnRepository inventoryGrnRepository;
	private InventoryGrnCommandFromApiJsonDeserializer inventoryGrnCommandFromApiJsonDeserializer;
	
	@Autowired
	public InventoryGrnDetailsWritePlatformServiceImp(final PlatformSecurityContext context,InventoryGrnRepository inventoryGrnRepository, InventoryGrnCommandFromApiJsonDeserializer inventoryGrnCommandFromApiJsonDeserializer) {
		this.context = context;
		this.inventoryGrnRepository = inventoryGrnRepository;
		this.inventoryGrnCommandFromApiJsonDeserializer = inventoryGrnCommandFromApiJsonDeserializer;
	}
	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(InventoryGrnDetailsWritePlatformServiceImp.class);	
		
		
	@Transactional
	@Override
	public CommandProcessingResult addGrnDetails(JsonCommand command) {
		InventoryGrn inventoryGrn = null;
		try{
			context.authenticatedUser();
			inventoryGrnCommandFromApiJsonDeserializer.validateForCreate(command.json());
			inventoryGrn = InventoryGrn.fromJson(command);
			this.inventoryGrnRepository.save(inventoryGrn);
		}catch(DataIntegrityViolationException dve){
			logger.error(dve.getMessage(), dve);   
			return new CommandProcessingResult(Long.valueOf(-1));
		
		}
		return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(inventoryGrn.getId()).build();
	}


     	
}
