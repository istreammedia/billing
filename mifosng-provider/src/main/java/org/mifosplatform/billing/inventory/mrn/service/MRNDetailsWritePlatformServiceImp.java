package org.mifosplatform.billing.inventory.mrn.service;

import java.text.ParseException;
import java.util.List;

import org.mifosplatform.billing.inventory.domain.InventoryItemDetails;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetailsRepository;
import org.mifosplatform.billing.inventory.mrn.api.MRNDetailsJpaRepository;
import org.mifosplatform.billing.inventory.mrn.data.MRNMoveDetailsData;
import org.mifosplatform.billing.inventory.mrn.domain.InventoryTransactionHistory;
import org.mifosplatform.billing.inventory.mrn.domain.InventoryTransactionHistoryJpaRepository;
import org.mifosplatform.billing.inventory.mrn.domain.MRNDetails;
import org.mifosplatform.billing.inventory.mrn.serialization.MRNDetailsCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MRNDetailsWritePlatformServiceImp implements MRNDetailsWritePlatformService{

	private final static Logger logger = (Logger) LoggerFactory.getLogger(MRNDetailsWritePlatformServiceImp.class);
	
	private final MRNDetailsJpaRepository mrnDetailsJpaRepository;
	private final PlatformSecurityContext context; 
	private final MRNDetailsCommandFromApiJsonDeserializer apiJsonDeserializer;
	private final MRNDetailsReadPlatformService mrnDetailsReadPlatformService;
	private final InventoryTransactionHistoryJpaRepository inventoryTransactionHistoryJpaRepository;
	private final InventoryItemDetailsRepository inventoryItemDetailsRepository;
	@Autowired
	public MRNDetailsWritePlatformServiceImp(final MRNDetailsJpaRepository mrnDetailsJpaRepository
			, final PlatformSecurityContext context
			, final MRNDetailsCommandFromApiJsonDeserializer apiJsonDeserializer
			,final MRNDetailsReadPlatformService mrnDetailsReadPlatformService
			, final InventoryTransactionHistoryJpaRepository inventoryTransactionHistoryJpaRepository
			,final InventoryItemDetailsRepository inventoryItemDetailsRepository) {
		this.mrnDetailsJpaRepository = mrnDetailsJpaRepository;
		this.context = context;
		this.apiJsonDeserializer = apiJsonDeserializer;
		this.mrnDetailsReadPlatformService = mrnDetailsReadPlatformService;
		this.inventoryTransactionHistoryJpaRepository = inventoryTransactionHistoryJpaRepository;
		this.inventoryItemDetailsRepository = inventoryItemDetailsRepository;
	}
	
	
	@Transactional
	@Override
	public CommandProcessingResult createMRNDetails(JsonCommand command) {
		context.authenticatedUser();
		apiJsonDeserializer.validateForCreate(command.json());
		try {
			
			MRNDetails mrnDetails = MRNDetails.formJson(command);
			mrnDetailsJpaRepository.save(mrnDetails);
			
			
		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command, dve);
		}catch (ParseException e) {
			throw new PlatformDataIntegrityException("invalid.date.format", "invalid.date.format", "purchaseDate");
		}
		return new CommandProcessingResultBuilder().withEntityId(command.commandId()).build();
	}
	
	@Transactional
	@Override
	public CommandProcessingResult moveMRN(JsonCommand command, Long mrnId) {
		context.authenticatedUser();
		apiJsonDeserializer.validateForMove(command.json());
		try {
			MRNMoveDetailsData mrnMoveDetailsData = MRNMoveDetailsData.fromJson(command);
			MRNDetails mrnDetails = mrnDetailsJpaRepository.findOne(mrnId);
			List<Long> itemMasterId = mrnDetailsReadPlatformService.retriveItemMasterId(mrnId);
			
			final List<String> serialNumber = mrnDetailsReadPlatformService.retriveSerialNumbers(mrnDetails.getFromOffice(),mrnId);
			
			if(!serialNumber.contains(mrnMoveDetailsData.getSerialNumber())){
				throw new PlatformDataIntegrityException("serialnumber.doesnot.exist", "serialnumber.doesnot.exist", "serialNumber","");
			}
			
			List<Long> itemDetailsId = mrnDetailsReadPlatformService.retriveItemDetailsId(mrnMoveDetailsData.getSerialNumber(), itemMasterId.get(0));
			InventoryItemDetails details = inventoryItemDetailsRepository.findOne(itemDetailsId.get(0));
			
			
			
			if(details.getOfficeId().equals(mrnDetails.getToOffice())){
				throw new PlatformDataIntegrityException("invalid.move.operation", "invalid.move.operation", "invalid.move.operation");
			}
			details.setOfficeId(mrnDetails.getToOffice());
			if(mrnDetails.getReceivedQuantity() < mrnDetails.getOrderdQuantity()){
				mrnDetails.setReceivedQuantity(mrnDetails.getReceivedQuantity()+1);
				mrnDetails.setStatus("Pending");
			} else if(mrnDetails.getReceivedQuantity().equals(mrnDetails.getOrderdQuantity())){
				throw new PlatformDataIntegrityException("received.quantity.is.full", "received.quantity.is.full", "received.quantity.is.full");
			}
			
			InventoryTransactionHistory transactionHistory = InventoryTransactionHistory.logTransaction(mrnMoveDetailsData.getMovedDate(), mrnMoveDetailsData.getMrnId(),"MRN", mrnMoveDetailsData.getSerialNumber(), itemMasterId.get(0), mrnDetails.getFromOffice(), mrnDetails.getToOffice());
			//InventoryTransactionHistory transactionHistory = InventoryTransactionHistory.logTransaction(mrnMoveDetailsData.getMovedDate(),mrnMoveDetailsData.getMrnId(),"MRN",mrnMoveDetailsData.getSerialNumber(),mrnDetails.getFromOffice(),mrnDetails.getToOffice(),itemMasterId.get(0));
			
			details.setOfficeId(mrnDetails.getToOffice());
			inventoryItemDetailsRepository.save(details);
			inventoryTransactionHistoryJpaRepository.save(transactionHistory);
			if(mrnDetails.getOrderdQuantity().equals(mrnDetails.getReceivedQuantity())){
				mrnDetails.setStatus("Completed");
			}
			mrnDetailsJpaRepository.save(mrnDetails);
			
			
		} catch (ParseException e) {
			throw new PlatformDataIntegrityException("invalid.moved.date", "invalid.moved.date", "invalid.moved.date");
		} catch (EmptyResultDataAccessException e) {
			throw new PlatformDataIntegrityException("serial.number.doest.not.exist", "serial.number.doest.not.exist", "serial.number.doest.not.exist");
		}
	
		
		/*
		 * take data from mrndetails update office id with tooffice id in itemdetails table then update mrntransaction history table 
		 * */
		
		return new CommandProcessingResultBuilder().withEntityId(command.entityId()).build();
	}
	
	
	
	private void handleDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {

        Throwable realCause = dve.getMostSpecificCause();
       if (realCause.getMessage().contains("serial_no_constraint")){
       	throw new PlatformDataIntegrityException("validation.error.msg.inventory.mrn.duplicate.entry", "validation.error.msg.inventory.mrn.duplicate.entry", "validation.error.msg.inventory.mrn.duplicate.entry","");
       	
       }
       logger.error(dve.getMessage(), dve);   	
}
}
