package org.mifosplatform.billing.inventory.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.association.data.HardwareAssociationData;
import org.mifosplatform.billing.association.service.HardwareAssociationReadplatformService;
import org.mifosplatform.billing.association.service.HardwareAssociationWriteplatformService;
import org.mifosplatform.billing.inventory.domain.InventoryGrn;
import org.mifosplatform.billing.inventory.domain.InventoryGrnRepository;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetails;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetailsAllocation;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetailsAllocationRepository;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetailsRepository;
import org.mifosplatform.billing.inventory.domain.ItemDetailsRepository;
import org.mifosplatform.billing.inventory.mrn.domain.InventoryTransactionHistory;
import org.mifosplatform.billing.inventory.mrn.domain.InventoryTransactionHistoryJpaRepository;
import org.mifosplatform.billing.inventory.serialization.InventoryItemAllocationCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.inventory.serialization.InventoryItemCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.onetimesale.domain.OneTimeSale;
import org.mifosplatform.billing.onetimesale.domain.OneTimeSaleRepository;
import org.mifosplatform.billing.onetimesale.service.OneTimeSaleReadPlatformService;
import org.mifosplatform.billing.transactionhistory.service.TransactionHistoryWritePlatformService;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatus;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatusRepository;
import org.mifosplatform.infrastructure.configuration.domain.GlobalConfigurationProperty;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
	 public final static String CONFIG_PROPERTY="Implicit Association";
	@Autowired
	public InventoryItemDetailsWritePlatformServiceImp(final InventoryItemDetailsReadPlatformService inventoryItemDetailsReadPlatformService, 
			final PlatformSecurityContext context, final InventoryGrnRepository inventoryitemRopository,final ItemDetailsRepository itemDetailsRepository, 
			final InventoryItemCommandFromApiJsonDeserializer inventoryItemCommandFromApiJsonDeserializer,final InventoryItemAllocationCommandFromApiJsonDeserializer inventoryItemAllocationCommandFromApiJsonDeserializer, 
			final InventoryItemDetailsAllocationRepository inventoryItemDetailsAllocationRepository,final OneTimeSaleReadPlatformService oneTimeSaleReadPlatformService, 
			final OneTimeSaleRepository oneTimeSaleRepository,final InventoryItemDetailsRepository inventoryItemDetailsRepository,final FromJsonHelper fromJsonHelper, 
			final UploadStatusRepository uploadStatusRepository,final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService,
			final InventoryTransactionHistoryJpaRepository inventoryTransactionHistoryJpaRepository,final GlobalConfigurationRepository  configurationRepository,
			final HardwareAssociationReadplatformService associationReadplatformService,final HardwareAssociationWriteplatformService associationWriteplatformService) 
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
					
					throw new PlatformDataIntegrityException("no.more.ordered.quantity","no.more.ordered.quantity","no.more.ordered.quantity");
				}
			}else{
				
				throw new PlatformDataIntegrityException("grnid.does.not.exist", "grnid.does.not.exist","grnid.does.not.exist","grnid.does.not.exist");
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
			InventoryItemDetailsAllocation inventoryItemDetailsAllocation=null;
			InventoryItemDetails inventoryItemDetails = null;
			try{
				context.authenticatedUser();
				
				this.context.authenticatedUser();
				inventoryItemAllocationCommandFromApiJsonDeserializer.validateForCreate(command.json());
				
				/*
				 * data comming from the client side is stored in inventoryItemAllocation
				 * */
				inventoryItemDetailsAllocation = InventoryItemDetailsAllocation.fromJson(command);

				/*
				 * trying to get Data(Id) from  b_item_detail where b_item_detail.serial_no=?";
				 */
				
				inventoryItemDetails = inventoryItemDetailsReadPlatformService.retriveInventoryItemDetail(inventoryItemDetailsAllocation.getSerialNumber(),inventoryItemDetailsAllocation.getItemMasterId());
				if(inventoryItemDetails.getClientId()!=null){
					if(inventoryItemDetails.getClientId()<=0){
					}else{
						throw new PlatformDataIntegrityException("invalid.serial.number1", "invalid.serial.number1","invalid.serial.number1");
					}
				}else{
					throw new PlatformDataIntegrityException("invalid.serial.number2", "invalid.serial.number2","invalid.serial.number2");
				}
				
				
				
				
				/**
				 * getting data from item_detail table based on item_master_id
				 */
				inventoryItemDetails = inventoryItemDetailsRepository.findOne(inventoryItemDetails.getItemMasterId());
				inventoryItemDetails.setItemMasterId(inventoryItemDetailsAllocation.getItemMasterId());
				inventoryItemDetails.setClientId(inventoryItemDetailsAllocation.getClientId());
				inventoryItemDetails.setStatus("Used");
				//InventoryItemDetailsAllocation serialNumbers = inventoryItemDetailsAllocationRepository.findOne(inventoryItemDetailsAllocation.getItemMasterId());
				
				this.inventoryItemDetailsRepository.save(inventoryItemDetails);
				this.inventoryItemDetailsAllocationRepository.save(inventoryItemDetailsAllocation);
				
		
				//For Plan And HardWare Association
				GlobalConfigurationProperty configurationProperty=this.configurationRepository.findOneByName(CONFIG_PROPERTY);
				
				/*if(configurationProperty.isEnabled())
				{
					List<HardwareAssociationData> associationDatas=this.associationReadplatformService.
							retrieveClientUnallocatePlanDetails(inventoryItemDetailsAllocation.getClientId());
					
					if(!associationDatas.isEmpty()){
						this.associationWriteplatformService.createNewHardwareAssociation(inventoryItemDetailsAllocation.getClientId(),
								associationDatas.get(0).getPlanId(),inventoryItemDetailsAllocation.getSerialNumber(),associationDatas.get(0).getorderId());
					}
				
				}*/
				
				
				
				
				OneTimeSale ots = this.oneTimeSaleRepository.findOne(inventoryItemDetailsAllocation.getOrderId());
				ots.setHardwareAllocated("ALLOCATED");
				this.oneTimeSaleRepository.save(ots);
				this.transactionHistoryWritePlatformService.saveTransactionHistory(ots.getClientId(), "HardwareAllocation", ots.getSaleDate(),"Units:"+ots.getUnits(),"ChargeCode:"+ots.getChargeCode(),"Quantity:"+ots.getQuantity(),"ItemId:"+ots.getItemId());						

				InventoryTransactionHistory transactionHistory = InventoryTransactionHistory.logTransaction(new LocalDate().toDate(), ots.getId(),"Allocation",inventoryItemDetailsAllocation.getSerialNumber(), inventoryItemDetailsAllocation.getItemMasterId(),inventoryItemDetails.getOfficeId(),inventoryItemDetailsAllocation.getClientId());
				inventoryTransactionHistoryJpaRepository.save(transactionHistory);
				
			}catch(DataIntegrityViolationException dve){
				handleDataIntegrityIssues(command, dve); 
				return new CommandProcessingResult(Long.valueOf(-1));
			}
			return new CommandProcessingResultBuilder().withCommandId(1L).withEntityId(inventoryItemDetailsAllocation.getId()).build();
			/*command is has to be changed to command.commandId() in the above code*/
		}
		
		
		public void genarateException(String uploadStatus,Long orderId,Long processRecords){
			String processStatus="New Unprocessed";
			String errormessage="item details already exist";
			LocalDate currentDate = new LocalDate();
			currentDate.toDate();
			if(uploadStatus.equalsIgnoreCase("UPLOADSTATUS")){
				UploadStatus uploadStatusObject = this.uploadStatusRepository.findOne(orderId);
				uploadStatusObject.update(currentDate,processStatus,processRecords,null,errormessage);
				this.uploadStatusRepository.save(uploadStatusObject);
			}
			
			 throw new PlatformDataIntegrityException("received.quantity.is.nill.hence.your.item.details.will.not.be.saved","","");
		}
}



