package org.mifosplatform.billing.onetimesale.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.adjustment.service.AdjustmentReadPlatformService;
import org.mifosplatform.billing.billingorder.commands.BillingOrderCommand;
import org.mifosplatform.billing.billingorder.commands.InvoiceTaxCommand;
import org.mifosplatform.billing.billingorder.data.BillingOrderData;
import org.mifosplatform.billing.billingorder.domain.BillingOrder;
import org.mifosplatform.billing.billingorder.domain.Invoice;
import org.mifosplatform.billing.billingorder.service.BillingOrderReadPlatformService;
import org.mifosplatform.billing.billingorder.service.BillingOrderWritePlatformService;
import org.mifosplatform.billing.billingorder.service.GenerateBill;
import org.mifosplatform.billing.billingorder.service.GenerateBillingOrderService;
import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;
import org.mifosplatform.billing.discountmaster.domain.DiscountMaster;
import org.mifosplatform.billing.discountmaster.domain.DiscountMasterRepository;
import org.mifosplatform.billing.onetimesale.data.OneTimeSaleData;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.billing.taxmaster.data.TaxMappingRateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceOneTimeSale {

	private final GenerateBill generateBill;
	private final BillingOrderReadPlatformService billingOrderReadPlatformService;
	private final BillingOrderWritePlatformService billingOrderWritePlatformService;
	private final GenerateBillingOrderService generateBillingOrderService;
	private final AdjustmentReadPlatformService adjustmentReadPlatformService;
	private final DiscountMasterRepository discountMasterRepository;
	@Autowired
	public InvoiceOneTimeSale(GenerateBill generateBill,BillingOrderReadPlatformService billingOrderReadPlatformService,
			BillingOrderWritePlatformService billingOrderWritePlatformService,GenerateBillingOrderService generateBillingOrderService,
			AdjustmentReadPlatformService adjustmentReadPlatformService,final DiscountMasterRepository discountMasterRepository) {
		this.generateBill = generateBill;
		this.billingOrderReadPlatformService = billingOrderReadPlatformService;
		this.billingOrderWritePlatformService = billingOrderWritePlatformService;
		this.generateBillingOrderService = generateBillingOrderService;
		this.adjustmentReadPlatformService = adjustmentReadPlatformService;
		this.discountMasterRepository=discountMasterRepository;
	}

	public void invoiceOneTimeSale(Long clientId, OneTimeSaleData oneTimeSaleData) {
		List<BillingOrderCommand> billingOrderCommands = new ArrayList<BillingOrderCommand>();
		
			// check whether one time sale is invoiced
			// N - not invoiced
			// y - invoiced
			if (oneTimeSaleData.getIsInvoiced().equalsIgnoreCase("N")) {
				BillingOrderData billingOrderData = new BillingOrderData(oneTimeSaleData.getId(),oneTimeSaleData.getClientId(),	new LocalDate().toDate(),
						oneTimeSaleData.getChargeCode(), "NRC",	oneTimeSaleData.getTotalPrice());
				List<InvoiceTaxCommand> listOfTaxes = calculateTax(billingOrderData);
				 
				DiscountMaster discountMaster=this.discountMasterRepository.findOne(oneTimeSaleData.getDiscountId());
				DiscountMasterData discountMasterData=new DiscountMasterData(discountMaster.getId(),discountMaster.getDiscountCode(),discountMaster.getDiscountDescription(),
						null,null,null,null);
				BillingOrderCommand billingOrderCommand = new BillingOrderCommand(billingOrderData.getClientOrderId(),new Long(0),billingOrderData.getClientId(),
						new LocalDate().toDate(), null,new LocalDate().toDate(), null,billingOrderData.getChargeCode(),	billingOrderData.getChargeType(), null,
						billingOrderData.getDurationType(), null,billingOrderData.getPrice(), null, listOfTaxes,new LocalDate().toDate(), new LocalDate().toDate(),discountMasterData,billingOrderData.getTaxInclusive());
				        billingOrderCommands.add(billingOrderCommand);
				List<BillingOrder> listOfBillingOrders = billingOrderWritePlatformService.createBillingProduct(billingOrderCommands);
				// calculation of invoice
				Invoice invoice = this.generateBillingOrderService.generateInvoice(billingOrderCommands);

				// To fetch record from client_balance table
				List<ClientBalanceData> clientBalancesDatas = adjustmentReadPlatformService.retrieveAllAdjustments(clientId);
				
				this.billingOrderWritePlatformService.updateClientBalance(invoice,clientBalancesDatas);

				// Insertion into invoice table
				//Invoice invoice = billingOrderWritePlatformService.createInvoice(invoiceCommand, clientBalancesDatas);

				// Updation of invoice id in invoice_tax table
				//billingOrderWritePlatformService.updateInvoiceTax(invoice,billingOrderCommands, listOfBillingOrders);

				// Updation of invoice id in charge table
				//billingOrderWritePlatformService.updateInvoiceCharge(invoice,listOfBillingOrders);
				
				// update column is_invoiceed of one-time-sale
				
			

			} else {

			}
		}
	

	/*public void updateOneTimeSale(OneTimeSaleData oneTimeSaleData) {
		EventOrder oneTimeSale = eventOrderRepository.findOne(oneTimeSaleData.getId());
		oneTimeSale.setInvoiced();
		eventOrderRepository.save(oneTimeSale);
		
		
		OneTimeSale oneTimeSale = oneTimeSaleRepository.findOne(oneTimeSaleData.getId());
		oneTimeSale.setDeleted('y');
		oneTimeSaleRepository.save(oneTimeSale);
		
	}*/

	public List<InvoiceTaxCommand> calculateTax(BillingOrderData billingOrderData) {

		List<TaxMappingRateData> taxMappingRateDatas = billingOrderReadPlatformService.retrieveTaxMappingDate(billingOrderData.getClientId(),billingOrderData.getChargeCode());
		if(taxMappingRateDatas.isEmpty()){
			taxMappingRateDatas = billingOrderReadPlatformService.retrieveDefaultTaxMappingDate(billingOrderData.getClientId(),billingOrderData.getChargeCode());
		}
		List<InvoiceTaxCommand> invoiceTaxCommands = generateBill.generateInvoiceTax(taxMappingRateDatas,billingOrderData.getPrice(),billingOrderData.getClientId());
		// List<InvoiceTax> listOfTaxes = billingOrderWritePlatformService.createInvoiceTax(invoiceTaxCommand);
		return invoiceTaxCommands;
	}

}
