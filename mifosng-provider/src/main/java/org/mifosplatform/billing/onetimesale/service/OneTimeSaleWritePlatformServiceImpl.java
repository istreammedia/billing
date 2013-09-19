package org.mifosplatform.billing.onetimesale.service;

import java.math.BigDecimal;
import java.util.List;

import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.item.domain.ItemMaster;
import org.mifosplatform.billing.item.domain.ItemRepository;
import org.mifosplatform.billing.item.service.ItemReadPlatformService;
import org.mifosplatform.billing.onetimesale.data.OneTimeSaleData;
import org.mifosplatform.billing.onetimesale.domain.OneTimeSale;
import org.mifosplatform.billing.onetimesale.domain.OneTimeSaleRepository;
import org.mifosplatform.billing.onetimesale.serialization.OneTimesaleCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.pricing.service.PriceReadPlatformService;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.billing.transactionhistory.service.TransactionHistoryWritePlatformService;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.api.JsonQuery;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class OneTimeSaleWritePlatformServiceImpl implements OneTimeSaleWritePlatformService{
	
	private final PlatformSecurityContext context;
	private final OneTimeSaleRepository  oneTimeSaleRepository;
	private final ItemRepository itemMasterRepository;
	
	private final OneTimesaleCommandFromApiJsonDeserializer apiJsonDeserializer;
	private final InvoiceOneTimeSale invoiceOneTimeSale;
	private final ItemReadPlatformService itemReadPlatformService;
	private final OneTimeSaleReadPlatformService oneTimeSaleReadPlatformService;
	private final FromJsonHelper fromJsonHelper;
	private final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService;
	private final PriceReadPlatformService priceReadPlatformService;
	
	
	@Autowired
	public OneTimeSaleWritePlatformServiceImpl(final PlatformSecurityContext context,final OneTimeSaleRepository oneTimeSaleRepository,
			final ItemRepository itemMasterRepository,final OneTimesaleCommandFromApiJsonDeserializer apiJsonDeserializer,
			final InvoiceOneTimeSale invoiceOneTimeSale,final ItemReadPlatformService itemReadPlatformService,final FromJsonHelper fromJsonHelper,
			final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService,final OneTimeSaleReadPlatformService oneTimeSaleReadPlatformService,
			final PriceReadPlatformService priceReadPlatformService)
	{
		this.context=context;
		this.oneTimeSaleRepository=oneTimeSaleRepository;
		this.itemMasterRepository=itemMasterRepository;
		this.apiJsonDeserializer=apiJsonDeserializer;
		this.invoiceOneTimeSale=invoiceOneTimeSale;
		this.itemReadPlatformService=itemReadPlatformService;
		this.oneTimeSaleReadPlatformService=oneTimeSaleReadPlatformService;
		this.fromJsonHelper=fromJsonHelper;
		this.transactionHistoryWritePlatformService = transactionHistoryWritePlatformService;
		this.priceReadPlatformService=priceReadPlatformService;
		
	}
	@Override
	public CommandProcessingResult createOneTimeSale(JsonCommand command,
			Long clientId) {
		try{
			this.context.authenticatedUser();
			this.apiJsonDeserializer.validateForCreate(command.json());
		    final Long itemId=command.longValueOfParameterNamed("itemId");
			ItemMaster item=this.itemMasterRepository.findOne(itemId);
			OneTimeSale oneTimeSale=OneTimeSale.fromJson(clientId,command,item);
			this.oneTimeSaleRepository.save(oneTimeSale);
			List<OneTimeSaleData> oneTimeSaleDatas = oneTimeSaleReadPlatformService.retrieveOnetimeSaleDate(clientId);
			for (OneTimeSaleData oneTimeSaleData : oneTimeSaleDatas) {
			this.invoiceOneTimeSale.invoiceOneTimeSale(clientId,oneTimeSaleData);
			updateOneTimeSale(oneTimeSaleData);
			}
			transactionHistoryWritePlatformService.saveTransactionHistory(oneTimeSale.getClientId(),"One Time Sale", oneTimeSale.getSaleDate(),
					"TotalPrice:"+oneTimeSale.getTotalPrice(),"Quantity:"+oneTimeSale.getQuantity(),"Units:"+oneTimeSale.getUnits(),"OneTimeSaleID:"+oneTimeSale.getId());
			return new CommandProcessingResult(Long.valueOf(oneTimeSale.getId()));
		} catch (DataIntegrityViolationException dve) {
			 handleCodeDataIntegrityIssues(command, dve);
			return new CommandProcessingResult(Long.valueOf(-1));
			
		}
		}
	
	private void handleCodeDataIntegrityIssues(JsonCommand command,DataIntegrityViolationException dve) {
		// TODO Auto-generated method stub
		
	}
	public void updateOneTimeSale(OneTimeSaleData oneTimeSaleData) {
		
		OneTimeSale oneTimeSale = oneTimeSaleRepository.findOne(oneTimeSaleData.getId());
		oneTimeSale.setDeleted('y');
		oneTimeSaleRepository.save(oneTimeSale);
		
	}
	
	@Override
	public ItemData calculatePrice(Long itemId, JsonQuery query) {

		try{
			this.context.authenticatedUser();
			this.apiJsonDeserializer.validateForPrice(query.parsedJson());
			final Integer quantity = fromJsonHelper.extractIntegerWithLocaleNamed("quantity", query.parsedJson());
			ItemMaster itemMaster=this.itemMasterRepository.findOne(itemId);
			if(itemMaster == null)
			{
				throw new RuntimeException();
			}
			BigDecimal TotalPrice=itemMaster.getUnitPrice().multiply(new BigDecimal(quantity));
			List<ItemData> itemCodeData = this.oneTimeSaleReadPlatformService.retrieveItemData();
			List<DiscountMasterData> discountdata = this.priceReadPlatformService.retrieveDiscountDetails();
			ItemData itemData = this.itemReadPlatformService.retrieveSingleItemDetails(itemId);
			return new ItemData(itemCodeData,itemData,TotalPrice,quantity,discountdata);
		}catch (DataIntegrityViolationException dve) {
			 handleCodeDataIntegrityIssues(null, dve);
			return null;
	}
		

}
}