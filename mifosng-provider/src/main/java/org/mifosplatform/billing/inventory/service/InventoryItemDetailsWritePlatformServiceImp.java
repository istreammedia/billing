package org.mifosplatform.billing.inventory.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.association.service.HardwareAssociationReadplatformService;
import org.mifosplatform.billing.association.service.HardwareAssociationWriteplatformService;
import org.mifosplatform.billing.hardwaremapping.service.HardwareMappingReadPlatformService;
import org.mifosplatform.billing.inventory.domain.InventoryGrn;
import org.mifosplatform.billing.inventory.domain.InventoryGrnRepository;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetails;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetailsAllocation;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetailsAllocationRepository;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetailsRepository;
import org.mifosplatform.billing.inventory.domain.ItemDetailsRepository;
import org.mifosplatform.billing.inventory.exception.OrderQuantityExceedsException;
import org.mifosplatform.billing.inventory.mrn.domain.InventoryTransactionHistory;
import org.mifosplatform.billing.inventory.mrn.domain.InventoryTransactionHistoryJpaRepository;
import org.mifosplatform.billing.inventory.serialization.InventoryItemAllocationCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.inventory.serialization.InventoryItemCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.item.domain.ItemRepository;
import org.mifosplatform.billing.onetimesale.domain.OneTimeSale;
import org.mifosplatform.billing.onetimesale.domain.OneTimeSaleRepository;
import org.mifosplatform.billing.onetimesale.service.OneTimeSaleReadPlatformService;
import org.mifosplatform.billing.order.domain.PlanHardwareMappingRepository;
import org.mifosplatform.billing.order.exceptions.NoGrnIdFoundException;
import org.mifosplatform.billing.transactionhistory.service.TransactionHistoryWritePlatformService;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatus;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatusRepository;
import org.mifosplatform.infrastructure.configuration.domain.GlobalConfigurationRepository;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


@Service
public class InventoryItemDetailsWritePlatformServiceImp implements InventoryItemDetailsWritePlatformService{
	
	
	private PlatformSecurityContext context;
	private InventoryItemDetailsRepository inventoryItemDetailsRepository;
	private InventoryGrnRepository inventoryGrnRepository;
	private ItemDetailsRepository itemDetailsRepository;
	private FromJsonHelper fromJsonHelper;
	private UploadStatusRepository uploadStatusRepository;
	private TransactionHistoryWritePlatformService transactionHistoryWritePlatformService;
	private InventoryItemCommandFromApiJsonDeserializer inventoryItemCommandFromApiJsonDeserializer;
	private InventoryItemAllocationCommandFromApiJsonDeserializer inventoryItemAllocationCommandFromApiJsonDeserializer;
	private InventoryItemDetailsAllocationRepository inventoryItemDetailsAllocationRepository; 
	private InventoryItemDetailsReadPlatformService inventoryItemDetailsReadPlatformService;
	//private OneTimeSaleReadPlatformServiceImpl oneTimeSaleReadPlatformServiceImpl;
	private OneTimeSaleRepository oneTimeSaleRepository;
	private InventoryTransactionHistoryJpaRepository inventoryTransactionHistoryJpaRepository;
	private GlobalConfigurationRepository configurationRepository;
	private HardwareAssociationReadplatformService associationReadplatformService;
	private HardwareAssociationWriteplatformService associationWriteplatformService;
	private final PlanHardwareMappingRepository hardwareMappingRepository;
	private final ItemRepository itemRepository;
	private final HardwareMappingReadPlatformService hardwareMappingReadPlatformService; 
	 public final static String CONFIG_PROPERTY="Implicit Association";
	@Autowired
	public InventoryItemDetailsWritePlatformServiceImp(final InventoryItemDetailsReadPlatformService inventoryItemDetailsReadPlatformService, 
			final PlatformSecurityContext context, final InventoryGrnRepository inventoryitemRopository,final ItemDetailsRepository itemDetailsRepository, 
			final InventoryItemCommandFromApiJsonDeserializer inventoryItemCommandFromApiJsonDeserializer,final InventoryItemAllocationCommandFromApiJsonDeserializer inventoryItemAllocationCommandFromApiJsonDeserializer, 
			final InventoryItemDetailsAllocationRepository inventoryItemDetailsAllocationRepository,final OneTimeSaleReadPlatformService oneTimeSaleReadPlatformService, 
			final OneTimeSaleRepository oneTimeSaleRepository,final InventoryItemDetailsRepository inventoryItemDetailsRepository,final FromJsonHelper fromJsonHelper, 
			final UploadStatusRepository uploadStatusRepository,final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService,
			final InventoryTransactionHistoryJpaRepository inventoryTransactionHistoryJpaRepository,final GlobalConfigurationRepository  configurationRepository,
			final HardwareAssociationReadplatformService associationReadplatformService,final HardwareAssociationWriteplatformService associationWriteplatformService,
			final PlanHardwareMappingRepository hardwareMappingRepository,final ItemRepository itemRepository,final HardwareMappingReadPlatformService hardwareMappingReadPlatformService) 
	{
		this.inventoryItemDetailsReadPlatformService = inventoryItemDetailsReadPlatformService;
		this.context=context;
		this.inventoryItemDetailsRepository=inventoryItemDetailsRepository;
		this.inventoryGrnRepository=inventoryitemRopository;
		this.itemDetailsRepository = itemDetailsRepository;
		this.inventoryItemCommandFromApiJsonDeserializer = inventoryItemCommandFromApiJsonDeserializer;
		this.inventoryItemAllocationCommandFromApiJsonDeserializer = inventoryItemAllocationCommandFromApiJsonDeserializer;
		this.inventoryItemDetailsAllocationRepository = inventoryItemDetailsAllocationRepository;
		//this.oneTimeSaleReadPlatformServiceImpl = oneTimeSaleReadPlatformServiceImpl;
		this.oneTimeSaleRepository = oneTimeSaleRepository;
		this.fromJsonHelper=fromJsonHelper;
		this.uploadStatusRepository=uploadStatusRepository;
		this.transactionHistoryWritePlatformService = transactionHistoryWritePlatformService;
		this.inventoryTransactionHistoryJpaRepository = inventoryTransactionHistoryJpaRepository;
		this.configurationRepository=configurationRepository;
		this.associationReadplatformService=associationReadplatformService;
		this.associationWriteplatformService=associationWriteplatformService;
		this.hardwareMappingRepository=hardwareMappingRepository;
		this.itemRepository=itemRepository;
		this.hardwareMappingReadPlatformService=hardwareMappingReadPlatformService;
	}
	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(InventoryItemDetailsWritePlatformServiceImp.class);
	
	
	
	@SuppressWarnings("unused")
	@Transactional
	@Override
	public CommandProcessingResult addItem(final JsonCommand command,Long flag) {

		
		InventoryItemDetails inventoryItemDetails=null;

		try{
			
			//context.authenticatedUser();
			
			this.context.authenticatedUser();
			
			inventoryItemCommandFromApiJsonDeserializer.validateForCreate(command);
			
			inventoryItemDetails = InventoryItemDetails.fromJson(command,fromJsonHelper);
			Long flag1 = command.longValueOfParameterNamed("flag");
			InventoryGrn inventoryGrn = inventoryGrnRepository.findOne(inventoryItemDetails.getGrnId());
			List<Long> itemMasterId = this.inventoryItemDetailsReadPlatformService.retriveSerialNumberForItemMasterId(inventoryItemDetails.getSerialNumber());
			if(itemMasterId.contains(inventoryItemDetails.getItemMasterId())){
				
				throw new PlatformDataIntegrityException("validation.error.msg.inventory.item.duplicate.serialNumber", "validation.error.msg.inventory.item.duplicate.serialNumber", "validation.error.msg.inventory.item.duplicate.serialNumber","validation.error.msg.inventory.item.duplicate.serialNumber");
			}
			 
			
			if(inventoryGrn != null){
				inventoryItemDetails.setOfficeId(inventoryGrn.getOfficeId());
				if(inventoryGrn.getReceivedQuantity() < inventoryGrn.getOrderdQuantity()){
					inventoryGrn.setReceivedQuantity(inventoryGrn.getReceivedQuantity()+1);
					this.inventoryGrnRepository.save(inventoryGrn);
				}else{
					
					throw new OrderQuantityExceedsException(inventoryGrn.getOrderdQuantity());
				}
			}else{
				
				throw new NoGrnIdFoundException(inventoryItemDetails.getGrnId());
			}
			this.inventoryItemDetailsRepository.save(inventoryItemDetails);
			InventoryTransactionHistory transactionHistory = InventoryTransactionHistory.logTransaction(new LocalDate().toDate(), inventoryItemDetails.getId(),"Item Detail",inventoryItemDetails.getSerialNumber(), inventoryItemDetails.getItemMasterId(), inventoryItemDetails.getGrnId(), inventoryGrn.getOfficeId());
			//InventoryTransactionHistory transactionHistory = InventoryTransactionHistory.logTransaction(new LocalDate().toDate(),inventoryItemDetails.getId(),"Item Detail",inventoryItemDetails.getSerialNumber(),inventoryGrn.getOfficeId(),inventoryItemDetails.getClientId(),inventoryItemDetails.getItemMasterId());
			inventoryTransactionHistoryJpaRepository.save(transactionHistory);
			/*++processRecords;
             processStatus="Processed";*/
			
			
		} catch (DataIntegrityViolationException dve){
			
			handleDataIntegrityIssues(command,dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}
			
		return new CommandProcessingResultBuilder().withEntityId(inventoryItemDetails.getGrnId()).build();
	}
	
	
		private void handleDataIntegrityIssues(final JsonCommand element, final DataIntegrityViolationException dve) {

	         Throwable realCause = dve.getMostSpecificCause();
	        if (realCause.getMessage().contains("serial_no_constraint")){
	        	throw new PlatformDataIntegrityException("validation.error.msg.inventory.item.duplicate.serialNumber", "validation.error.msg.inventory.item.duplicate.serialNumber", "validation.error.msg.inventory.item.duplicate.serialNumber","");
	        	
	        }


	        logger.error(dve.getMessage(), dve);   	
	}




		@Override
		public CommandProcessingResult allocateHardware(JsonCommand command) {
			Long id = null;
			try{
				context.authenticatedUser();
				
				this.context.authenticatedUser();
				inventoryItemAllocationCommandFromApiJsonDeserializer.validateForCreate(command.json());
				
				/*
				 * data comming from the client side is stored in inventoryItemAllocation
				 * */
				 final JsonElement element = fromJsonHelper.parse(command.json());
			        
			        
			        JsonArray allocationData = fromJsonHelper.extractJsonArrayNamed("serialNumber", element);
			        int i=1;
			        for(JsonElement j:allocationData){
			        	
			        	InventoryItemDetailsAllocation inventoryItemDetailsAllocation=null;
						InventoryItemDetails inventoryItemDetails = null;
			        	inventoryItemDetailsAllocation = InventoryItemDetailsAllocation.fromJson(j,fromJsonHelper);
			        	try{
							inventoryItemDetails = inventoryItemDetailsReadPlatformService.retriveInventoryItemDetail(inventoryItemDetailsAllocation.getSerialNumber(),inventoryItemDetailsAllocation.getItemMasterId());
							if(inventoryItemDetails.getClientId()!=null){
								if(inventoryItemDetails.getClientId()<=0){
								}else{
									throw new PlatformDataIntegrityException("SerialNumber "+inventoryItemDetailsAllocation.getSerialNumber()+" already exist.", "SerialNumber "+inventoryItemDetailsAllocation.getSerialNumber()+ "already exist.","serialNumber"+i);
								}
							}else{
								throw new PlatformDataIntegrityException("invalid.serial.number2", "invalid.serial.number2","invalid.serial.number2");
							}
							}catch(EmptyResultDataAccessException e){
								throw new PlatformDataIntegrityException("SerialNumber SerialNumber"+i+" doest not exist.","SerialNumber SerialNumber"+i+" doest not exist.","serialNumber"+i);
							}
			        	
			        	inventoryItemDetails = inventoryItemDetailsRepository.findOne(inventoryItemDetails.getItemMasterId());
						inventoryItemDetails.setItemMasterId(inventoryItemDetailsAllocation.getItemMasterId());
						inventoryItemDetails.setClientId(inventoryItemDetailsAllocation.getClientId());
						inventoryItemDetails.setStatus("Used");
						
						
						this.inventoryItemDetailsRepository.save(inventoryItemDetails);
						this.inventoryItemDetailsRepository.flush();
						this.inventoryItemDetailsAllocationRepository.save(inventoryItemDetailsAllocation);
						this.inventoryItemDetailsAllocationRepository.flush();
						OneTimeSale ots = this.oneTimeSaleRepository.findOne(inventoryItemDetailsAllocation.getOrderId());
						ots.setHardwareAllocated("ALLOCATED");
						this.oneTimeSaleRepository.save(ots);
						this.oneTimeSaleRepository.flush();
						this.transactionHistoryWritePlatformService.saveTransactionHistory(ots.getClientId(), "HardwareAllocation", ots.getSaleDate(),"Units:"+ots.getUnits(),"ChargeCode:"+ots.getChargeCode(),"Quantity:"+ots.getQuantity(),"ItemId:"+ots.getItemId());

						InventoryTransactionHistory transactionHistory = InventoryTransactionHistory.logTransaction(new LocalDate().toDate(), ots.getId(),"Allocation",inventoryItemDetailsAllocation.getSerialNumber(), inventoryItemDetailsAllocation.getItemMasterId(),inventoryItemDetails.getOfficeId(),inventoryItemDetailsAllocation.getClientId());
						
						inventoryTransactionHistoryJpaRepository.save(transactionHistory);
						inventoryTransactionHistoryJpaRepository.flush();
						id = inventoryItemDetailsAllocation.getId();
						i++;
			        }
				
			}catch(DataIntegrityViolationException dve){
				handleDataIntegrityIssues(command, dve); 
				return new CommandProcessingResult(Long.valueOf(-1));
			}
			return new CommandProcessingResultBuilder().withCommandId(1L).withEntityId(id).build();
			/*command is has to be changed to command.commandId() in the above code*/
		}
		
		
		public void genarateException(String uploadStatus,Long orderId,Long processRecords){
			String processStatus="New Unprocessed";
			String errormessage="item details already exist";
			LocalDate currentDate = new LocalDate();
			currentDate.toDate();
			if(uploadStatus.equalsIgnoreCase("UPLOADSTATUS")){
				UploadStatus uploadStatusObject = this.uploadStatusRepository.findOne(orderId);
				uploadStatusObject.update(currentDate,processStatus,processRecords,null,errormessage,null);
				this.uploadStatusRepository.save(uploadStatusObject);
			}
			
			 throw new PlatformDataIntegrityException("received.quantity.is.nill.hence.your.item.details.will.not.be.saved","","");
		}
}



