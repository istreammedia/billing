package org.mifosplatform.billing.billingorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.billingorder.commands.BillingOrderCommand;
import org.mifosplatform.billing.billingorder.commands.InvoiceTaxCommand;
import org.mifosplatform.billing.billingorder.data.BillingOrderData;
import org.mifosplatform.billing.billingorder.domain.BillingOrder;
import org.mifosplatform.billing.billingorder.domain.Invoice;
import org.mifosplatform.billing.billingorder.domain.InvoiceRepository;
import org.mifosplatform.billing.billingorder.domain.InvoiceTax;
import org.mifosplatform.billing.discountmaster.domain.DiscountMaster;
import org.mifosplatform.billing.discountmaster.domain.DiscountMasterRepository;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateReverseBillingOrderServiceImplementation implements
		GenerateReverseBillingOrderService {

	private final BillingOrderReadPlatformService billingOrderReadPlatformService;
	private final GenerateDisconnectionBill generateDisconnectionBill;
	private final InvoiceRepository invoiceRepository;
	private final DiscountMasterRepository discountMasterRepository;

	
	@Autowired
	public GenerateReverseBillingOrderServiceImplementation(BillingOrderReadPlatformService billingOrderReadPlatformService,
			GenerateDisconnectionBill generateDisconnectionBill,InvoiceRepository invoiceRepository,DiscountMasterRepository discountMasterRepository) {

		this.billingOrderReadPlatformService = billingOrderReadPlatformService;
		this.generateDisconnectionBill = generateDisconnectionBill;
		this.invoiceRepository = invoiceRepository;
		this.discountMasterRepository = discountMasterRepository;
	}

	@Override
	public List<BillingOrderCommand> generateReverseBillingOrder(
			List<BillingOrderData> billingOrderProducts,
			LocalDate disconnectDate) {

		BillingOrderCommand billingOrderCommand = null;
		List<BillingOrderCommand> billingOrderCommands = new ArrayList<BillingOrderCommand>();
		if (billingOrderProducts.size() != 0) {

			for (BillingOrderData billingOrderData : billingOrderProducts) {

				DiscountMasterData discountMasterData = null;

				List<DiscountMasterData> discountMasterDatas = billingOrderReadPlatformService
						.retrieveDiscountOrders(
								billingOrderData.getClientOrderId(),
								billingOrderData.getOderPriceId());

				if (discountMasterDatas.size() != 0) {
					discountMasterData = discountMasterDatas.get(0);
				}

				if (generateDisconnectionBill.isChargeTypeRC(billingOrderData)) {

					System.out.println("---- RC ----");

					// monthly
					if (billingOrderData.getDurationType().equalsIgnoreCase("month(s)")) {

						billingOrderCommand = generateDisconnectionBill.getReverseMonthyBill(billingOrderData,discountMasterData, disconnectDate);
						billingOrderCommands.add(billingOrderCommand);

					}

					// weekly
				 else if (billingOrderData.getDurationType().equalsIgnoreCase(
						"week(s)")) {

					if (billingOrderData.getBillingAlign()
							.equalsIgnoreCase("N")) {
						billingOrderCommand = generateDisconnectionBill.getReverseWeeklyBill(billingOrderData,discountMasterData,disconnectDate);
						billingOrderCommands.add(billingOrderCommand);

					}
				 }
				}
			}

		}

		return billingOrderCommands;
	}

	@Override
	public Invoice generateNegativeInvoice(List<BillingOrderCommand> billingOrderCommands) {
		BigDecimal totalChargeAmountForServices = BigDecimal.ZERO;
		//BigDecimal totalTaxAmountForServices = BigDecimal.ZERO;
		
		BigDecimal invoiceAmount = BigDecimal.ZERO;
		BigDecimal totalChargeAmount = BigDecimal.ZERO;
		BigDecimal netTaxAmount = BigDecimal.ZERO;
		
		LocalDate invoiceDate = new LocalDate();
		List<BillingOrder> charges = new ArrayList<BillingOrder>();
		
		Invoice invoice = new Invoice(billingOrderCommands.get(0).getClientId(), new LocalDate().toDate(), invoiceAmount, invoiceAmount, netTaxAmount, "active",
				null, null, null, null);
		
		for (BillingOrderCommand billingOrderCommand : billingOrderCommands) {
			BigDecimal netChargeTaxAmount = BigDecimal.ZERO;
			BigDecimal discountAmount = billingOrderCommand.getDiscountMasterData().getDiscountAmount();
			
			BigDecimal netChargeAmount = billingOrderCommand.getPrice().subtract(discountAmount);
			
			
			DiscountMaster discountMaster = null;
			if(billingOrderCommand.getDiscountMasterData()!= null){
				discountMaster = this.discountMasterRepository.findOne(billingOrderCommand.getDiscountMasterData().getDiscountMasterId());
			}
			
			List<InvoiceTaxCommand> invoiceTaxCommands = billingOrderCommand.getListOfTax();

			BillingOrder charge = new BillingOrder(billingOrderCommand.getClientId(), billingOrderCommand.getClientOrderId(), billingOrderCommand.getOrderPriceId(),
					billingOrderCommand.getChargeCode(),billingOrderCommand.getChargeType(),discountMaster.getDiscountCode(), billingOrderCommand.getPrice(), discountAmount,
					netChargeAmount, billingOrderCommand.getStartDate(), billingOrderCommand.getEndDate());
			
			for(InvoiceTaxCommand invoiceTaxCommand : invoiceTaxCommands){
				
				netChargeTaxAmount = netChargeTaxAmount.add(invoiceTaxCommand.getTaxAmount());
				
				InvoiceTax invoiceTax = new InvoiceTax(invoice, charge, invoiceTaxCommand.getTaxCode(),
						invoiceTaxCommand.getTaxValue(), invoiceTaxCommand.getTaxPercentage(), invoiceTaxCommand.getTaxAmount());
				charge.addChargeTaxes(invoiceTax);
				
			}
			
			
			if(billingOrderCommand.getTaxInclusive()!=null){
				
				if(isTaxInclusive(billingOrderCommand.getTaxInclusive())){
					netChargeAmount = netChargeAmount.subtract(netChargeTaxAmount);
					charge.setNetChargeAmount(netChargeAmount.negate());
				}
			}
			netTaxAmount = netTaxAmount.add(netChargeTaxAmount);
			totalChargeAmount = totalChargeAmount.add(netChargeAmount);
			
			invoice.addCharges(charge);		
			
		}

		
		invoiceAmount = totalChargeAmount.add(netTaxAmount);
		invoice.setNetChargeAmount(totalChargeAmount.negate());
		invoice.setTaxAmount(netTaxAmount.negate());
		invoice.setInvoiceAmount(invoiceAmount.negate());
		return this.invoiceRepository.save(invoice);
	}
	
	public Boolean isTaxInclusive(Integer taxInclusive){
		
		Boolean isTaxInclusive = false;
		if(taxInclusive == 1) isTaxInclusive = true;

		return isTaxInclusive;
	}
}
