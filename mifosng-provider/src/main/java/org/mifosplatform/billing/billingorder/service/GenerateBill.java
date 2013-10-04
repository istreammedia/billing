package org.mifosplatform.billing.billingorder.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.mifosplatform.billing.billingorder.commands.BillingOrderCommand;
import org.mifosplatform.billing.billingorder.commands.InvoiceTaxCommand;
import org.mifosplatform.billing.billingorder.data.BillingOrderData;
import org.mifosplatform.billing.order.service.ClientRegionDetails;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.billing.taxmaster.data.TaxMappingRateData;
import org.mifosplatform.infrastructure.configuration.domain.GlobalConfigurationProperty;
import org.mifosplatform.infrastructure.configuration.domain.GlobalConfigurationRepository;
import org.mifosplatform.infrastructure.configuration.exception.GlobalConfigurationPropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateBill {

	private final BillingOrderReadPlatformService billingOrderReadPlatformService;
	private final InvoiceTaxPlatformService invoiceTaxPlatformService;
    private final ClientRegionDetails clientRegionDetails;
    private final GlobalConfigurationRepository globalConfigurationRepository;
	// private final OrderRepository orderRepository;

	@Autowired
	public GenerateBill(BillingOrderReadPlatformService billingOrderReadPlatformService,InvoiceTaxPlatformService invoiceTaxPlatformService,
			final ClientRegionDetails clientRegionDetails,final GlobalConfigurationRepository globalConfigurationRepository
	/* final OrderRepository orderRepository */) {
		this.billingOrderReadPlatformService = billingOrderReadPlatformService;
		this.invoiceTaxPlatformService = invoiceTaxPlatformService;
		this.clientRegionDetails=clientRegionDetails;
		this.globalConfigurationRepository=globalConfigurationRepository;
	}

	BigDecimal pricePerMonth = null;
	LocalDate startDate = null;
	LocalDate endDate = null;
	BigDecimal price = null;
	LocalDate invoiceTillDate = null;
	LocalDate nextbillDate = null;
	BillingOrderCommand billingOrderCommand = null;

	public boolean isChargeTypeNRC(BillingOrderData billingOrderData) {
		boolean chargeType = false;
		if (billingOrderData.getChargeType().equals("NRC")) {
			chargeType = true;
		}
		return chargeType;
	}

	public boolean isChargeTypeRC(BillingOrderData billingOrderData) {
		boolean chargeType = false;
		if (billingOrderData.getChargeType().equals("RC")) {
			chargeType = true;
		}
		return chargeType;
	}

	public boolean isChargeTypeUC(BillingOrderData billingOrderData) {
		boolean chargeType = false;
		if (billingOrderData.getChargeType().equals("UC")) {
			chargeType = true;
		}
		return chargeType;
	}

	// prorata monthly bill
	public BillingOrderCommand getProrataMonthlyFirstBill(
			BillingOrderData billingOrderData, DiscountMasterData discountMasterData) {
		BigDecimal discountAmount = BigDecimal.ZERO;
		startDate = new LocalDate(billingOrderData.getBillStartDate());

		endDate = startDate.dayOfMonth().withMaximumValue();
		
		 

		if (endDate.toDate().before(billingOrderData.getBillEndDate())) {
			int currentDay = startDate.getDayOfMonth();
			int endOfMonth = startDate.dayOfMonth().withMaximumValue()
					.getDayOfMonth();
			int totalDays = endOfMonth - currentDay + 1;
			// price = billingOrderData.getPrice();
			price = billingOrderData.getPrice().setScale(Integer.parseInt(roundingDecimal()));
			BigDecimal pricePerDay = price.divide(new BigDecimal(30), Integer.parseInt(roundingDecimal()),RoundingMode.HALF_UP);

			if (totalDays < endOfMonth) {
				price = pricePerDay.multiply(new BigDecimal(totalDays));
			}

		} else if (endDate.toDate().after(billingOrderData.getBillEndDate())) {
			endDate = new LocalDate(billingOrderData.getBillEndDate());
			price = getDisconnectionCredit(startDate, endDate,
					billingOrderData.getPrice(),
					billingOrderData.getDurationType());
		}

		invoiceTillDate = endDate;
		nextbillDate = invoiceTillDate.plusDays(1);
		
		if(this.isDiscountApplicable(startDate,discountMasterData,endDate)){
			
			discountMasterData = this.calculateDiscount(discountMasterData, discountAmount, price);
			
		}

		List<InvoiceTaxCommand> listOfTaxes = this.calculateTax(billingOrderData,price);

		return this.createBillingOrderCommand(billingOrderData, startDate, endDate, invoiceTillDate, nextbillDate, price, listOfTaxes,discountMasterData);  

	}

	public BillingOrderCommand getNextMonthBill(
			BillingOrderData billingOrderData, DiscountMasterData discountMasterData) {
		BigDecimal discountAmount = BigDecimal.ZERO;
		startDate = new LocalDate(billingOrderData.getNextBillableDate());
		endDate = new LocalDate(billingOrderData.getInvoiceTillDate())
				.plusMonths(billingOrderData.getChargeDuration()).dayOfMonth()
				.withMaximumValue();

		if (endDate.toDate().before(billingOrderData.getBillEndDate())) {
			price = billingOrderData.getPrice();
		} else if (endDate.toDate().after(billingOrderData.getBillEndDate())) {
			endDate = new LocalDate(billingOrderData.getBillEndDate());
			price = getDisconnectionCredit(startDate, endDate,
					billingOrderData.getPrice(),
					billingOrderData.getDurationType());
		}

		invoiceTillDate = endDate;
		nextbillDate = invoiceTillDate.plusDays(1);
		
		if(this.isDiscountApplicable(startDate,discountMasterData,endDate)){
			
			discountMasterData = this.calculateDiscount(discountMasterData, discountAmount, price);
			
		}

		List<InvoiceTaxCommand> listOfTaxes = this.calculateTax(billingOrderData,price);

		return this.createBillingOrderCommand(billingOrderData, startDate, endDate, invoiceTillDate, nextbillDate, price, listOfTaxes,discountMasterData);

	}

	// Generate Invoice Tax
	public List<InvoiceTaxCommand> generateInvoiceTax(List<TaxMappingRateData> taxMappingRateDatas, BigDecimal price,Long clientId) {

		BigDecimal taxPercentage = null;
		String taxCode = null;
		BigDecimal taxAmount = null;
		List<InvoiceTaxCommand> invoiceTaxCommands = new ArrayList<InvoiceTaxCommand>();
		InvoiceTaxCommand invoiceTaxCommand = null;
		if (taxMappingRateDatas != null) {

			for (TaxMappingRateData taxMappingRateData : taxMappingRateDatas) {

				taxPercentage = taxMappingRateData.getRate();
				taxCode = taxMappingRateData.getTaxCode();
				taxAmount = price.multiply(taxPercentage.divide(new BigDecimal(
						100)));

				invoiceTaxCommand = new InvoiceTaxCommand(clientId, null, null,
						taxCode, null, taxPercentage, taxAmount);
				invoiceTaxCommands.add(invoiceTaxCommand);
			}

		}
		return invoiceTaxCommands;

	}

	// Monthly Bill
	public BillingOrderCommand getMonthyBill(BillingOrderData billingOrderData, DiscountMasterData discountMasterData) {
		BigDecimal discountAmount = BigDecimal.ZERO.setScale(Integer.parseInt(roundingDecimal()));;
		if (billingOrderData.getInvoiceTillDate() == null) {
			startDate = new LocalDate(billingOrderData.getBillStartDate());
			endDate = startDate
					.plusMonths(billingOrderData.getChargeDuration())
					.minusDays(1);
			price = billingOrderData.getPrice();
		} else if (billingOrderData.getInvoiceTillDate() != null) {

			startDate = new LocalDate(billingOrderData.getNextBillableDate());
			endDate = startDate
					.plusMonths(billingOrderData.getChargeDuration())
					.minusDays(1);

			if (endDate.toDate().before(billingOrderData.getBillEndDate())) {
				price = billingOrderData.getPrice();
			} else if (endDate.toDate()
					.after(billingOrderData.getBillEndDate())) {
				endDate = new LocalDate(billingOrderData.getBillEndDate());
				price = getDisconnectionCredit(startDate, endDate,
						billingOrderData.getPrice(),
						billingOrderData.getDurationType());
			} else if (billingOrderData.getOrderStatus() == 3) {

			}
		}

		invoiceTillDate = endDate;
		nextbillDate = invoiceTillDate.plusDays(1);
		
		if(this.isDiscountApplicable(startDate,discountMasterData,endDate)){
			
			discountMasterData = this.calculateDiscount(discountMasterData, discountAmount, price);
			
		}

		// List<InvoiceTax> listOfTaxes = this.calculateTax(billingOrderData,price);
		List<InvoiceTaxCommand> listOfTaxes = this.calculateTax(billingOrderData,price);
		
		return this.createBillingOrderCommand(billingOrderData, startDate, endDate, invoiceTillDate, nextbillDate, price, listOfTaxes,discountMasterData);

	}

	// Pro rate Weekly Bill
	public BillingOrderCommand getProrataWeeklyFirstBill(
			BillingOrderData billingOrderData, DiscountMasterData discountMasterData) {
		BigDecimal discountAmount = BigDecimal.ZERO;
		startDate = new LocalDate(billingOrderData.getBillStartDate());
		endDate = startDate.dayOfWeek().withMaximumValue();

		int startDateOfWeek = startDate.getDayOfMonth();
		int endDateOfWeek = startDate.dayOfWeek().withMaximumValue().getDayOfMonth();

		int totalDays = 0;

		totalDays = Days.daysBetween(startDate, endDate).getDays() + 1;

		BigDecimal weeklyPricePerDay = getWeeklyPricePerDay(billingOrderData);

		Integer billingDays = 7 * billingOrderData.getChargeDuration();

		if (totalDays < billingDays) {
			price = weeklyPricePerDay.multiply(new BigDecimal(totalDays));
		} else if (totalDays == billingDays) {
			price = billingOrderData.getPrice();
		}

		invoiceTillDate = endDate;
		nextbillDate = endDate.plusDays(1);
		
		if(this.isDiscountApplicable(startDate,discountMasterData,endDate)){
			
			discountMasterData = this.calculateDiscount(discountMasterData, discountAmount, price);
			
		}

		List<InvoiceTaxCommand> listOfTaxes = this.calculateTax(billingOrderData,price);

		return this.createBillingOrderCommand(billingOrderData, startDate, endDate, invoiceTillDate, nextbillDate, price, listOfTaxes,discountMasterData);

	}

	public BillingOrderCommand getNextWeeklyBill(
			BillingOrderData billingOrderData, DiscountMasterData discountMasterData) {
		BigDecimal discountAmount = BigDecimal.ZERO;
		startDate = new LocalDate(billingOrderData.getNextBillableDate());

		endDate = startDate.dayOfWeek().withMaximumValue();
		if (billingOrderData.getChargeDuration() == 2) {
			endDate = endDate.plusWeeks(1);
		}

		if (endDate.toDate().before(billingOrderData.getBillEndDate())) {
			price = billingOrderData.getPrice();
		} else if (endDate.toDate().after(billingOrderData.getBillEndDate())) {
			endDate = new LocalDate(billingOrderData.getBillEndDate());
			price = getDisconnectionCredit(startDate, endDate,
					billingOrderData.getPrice(),
					billingOrderData.getDurationType());
		}

		invoiceTillDate = endDate;
		nextbillDate = endDate.plusDays(1);
		
		if(this.isDiscountApplicable(startDate,discountMasterData,endDate)){
			
			discountMasterData = this.calculateDiscount(discountMasterData, discountAmount, price);
			
		}

		List<InvoiceTaxCommand> listOfTaxes = this.calculateTax(billingOrderData,price); listOfTaxes = this.calculateTax(billingOrderData,price);

		return this.createBillingOrderCommand(billingOrderData, startDate, endDate, invoiceTillDate, nextbillDate, price, listOfTaxes,discountMasterData);

	}

	// Weekly Bill
	public BillingOrderCommand getWeeklyBill(BillingOrderData billingOrderData, DiscountMasterData discountMasterData) {
		BigDecimal discountAmount = BigDecimal.ZERO;
		if (billingOrderData.getInvoiceTillDate() == null) {

			// please consider the contract start date over here
			startDate = new LocalDate(billingOrderData.getBillStartDate());
			endDate = startDate.plusWeeks(1).minusDays(1);
			price = billingOrderData.getPrice().setScale(Integer.parseInt(roundingDecimal()));;
		} else if (billingOrderData.getInvoiceTillDate() != null) {

			startDate = new LocalDate(billingOrderData.getNextBillableDate());
			endDate = startDate.plusWeeks(1).minusDays(1);

			if (endDate.toDate().before(billingOrderData.getBillEndDate())) {
				price = billingOrderData.getPrice();
			} else if (endDate.toDate()
					.after(billingOrderData.getBillEndDate())) {
				endDate = new LocalDate(billingOrderData.getBillEndDate());
				price = getDisconnectionCredit(startDate, endDate,
						billingOrderData.getPrice(),
						billingOrderData.getDurationType());
			}
		}

		invoiceTillDate = endDate;
		nextbillDate = invoiceTillDate.plusDays(1);
		
		price = billingOrderData.getPrice();
		
		
		if(this.isDiscountApplicable(startDate,discountMasterData,endDate)){
			
			discountMasterData = this.calculateDiscount(discountMasterData, discountAmount, price);
			
		}
		
		List<InvoiceTaxCommand> listOfTaxes = this.calculateTax(billingOrderData,price);

		return this.createBillingOrderCommand(billingOrderData, startDate, endDate, invoiceTillDate, nextbillDate, price, listOfTaxes,discountMasterData);
	}

	// One Time Bill
	public BillingOrderCommand getOneTimeBill(BillingOrderData billingOrderData, DiscountMasterData discountMasterData) {

		LocalDate startDate = new LocalDate(billingOrderData.getBillStartDate());
		LocalDate endDate = startDate;
		LocalDate invoiceTillDate = startDate;
		LocalDate nextbillDate = invoiceTillDate.plusDays(1);
		BigDecimal price = billingOrderData.getPrice().setScale(Integer.parseInt(roundingDecimal()));

		List<InvoiceTaxCommand> listOfTaxes = this.calculateTax(billingOrderData,price);

		return this.createBillingOrderCommand(billingOrderData, startDate, endDate, invoiceTillDate, nextbillDate, price, listOfTaxes,discountMasterData);
	}

	// Disconnection credit price
	private BigDecimal getDisconnectionCredit(LocalDate startDate,
			LocalDate endDate, BigDecimal amount, String durationType) {

		int currentDay = startDate.getDayOfMonth();

		int totalDays = 0;
		if (startDate.isEqual(endDate)) {
			totalDays = 0;
		} else {
			totalDays = Days.daysBetween(startDate, endDate).getDays() + 1;
		}
		pricePerMonth = amount;
		BigDecimal pricePerDay = BigDecimal.ZERO.setScale(Integer.parseInt(roundingDecimal()));

		if (durationType.equalsIgnoreCase("month(s)")) {
			pricePerDay = pricePerMonth.divide(new BigDecimal(30), 2,
					RoundingMode.HALF_UP);

		} else if (durationType.equalsIgnoreCase("week(s)")) {
			pricePerDay = pricePerMonth.divide(new BigDecimal(7), 2,
					RoundingMode.HALF_UP);
		}

		return pricePerDay.multiply(new BigDecimal(totalDays));

	}

	// order cancelled bill
	public BillingOrderCommand getCancelledOrderBill(
			BillingOrderData billingOrderData, DiscountMasterData discountMasterData) {

		if (billingOrderData.getInvoiceTillDate() == null)
			startDate = new LocalDate(billingOrderData.getStartDate());
		else
			startDate = new LocalDate(billingOrderData.getNextBillableDate());

		endDate = new LocalDate(billingOrderData.getBillEndDate());

		price = this.getDisconnectionCredit(startDate, endDate,billingOrderData.getPrice(), billingOrderData.getDurationType());

		nextbillDate = new LocalDate().plusYears(1000);

		invoiceTillDate = endDate;
		
		List<InvoiceTaxCommand> listOfTaxes = this.calculateTax(billingOrderData,price);

		return this.createBillingOrderCommand(billingOrderData, startDate, endDate, invoiceTillDate, nextbillDate, price, listOfTaxes,discountMasterData);

	}

	// Per day weekly price
	public BigDecimal getWeeklyPricePerDay(BillingOrderData billingOrderData) {
		Integer billingDays = 7 * billingOrderData.getChargeDuration();

		return billingOrderData.getPrice().divide(new BigDecimal(billingDays),
				2, RoundingMode.HALF_UP);
	}

	// Daily Bill 
	public BillingOrderCommand getDailyBill(BillingOrderData billingOrderData, DiscountMasterData discountMasterData) {

		startDate = new LocalDate(billingOrderData.getBillStartDate());
		endDate = startDate;
		invoiceTillDate = endDate;
		nextbillDate = invoiceTillDate.plusDays(1);
		price = billingOrderData.getPrice();

		List<InvoiceTaxCommand> listOfTaxes = this.calculateTax(billingOrderData,price);

		return this.createBillingOrderCommand(billingOrderData, startDate, endDate, invoiceTillDate, nextbillDate, price, listOfTaxes,discountMasterData);

	}

	// Tax Calculation
	public List<InvoiceTaxCommand> calculateTax(BillingOrderData billingOrderData,BigDecimal billPrice) {
		
		//String clientRegion=this.clientRegionDetails.getTheClientRegionDetails(billingOrderData.getClientId());
		
		List<TaxMappingRateData> taxMappingRateDatas = billingOrderReadPlatformService.retrieveTaxMappingDate(billingOrderData.getClientId(),billingOrderData.getChargeCode());
		
		if(taxMappingRateDatas.isEmpty()){
			 taxMappingRateDatas = billingOrderReadPlatformService.retrieveDefaultTaxMappingDate(billingOrderData.getClientId(),billingOrderData.getChargeCode());
		}
		List<InvoiceTaxCommand> invoiceTaxCommand = generateInvoiceTax(taxMappingRateDatas, billPrice, billingOrderData.getClientId());
		//List<InvoiceTax> listOfTaxes = invoiceTaxPlatformService.createInvoiceTax(invoiceTaxCommand);
		return invoiceTaxCommand;
	}

	// Discount Applicable Logic
	public Boolean isDiscountApplicable(LocalDate chargeStartDate,DiscountMasterData discountMasterData,LocalDate chargeEndDate) {
		boolean isDiscountApplicable = false;

		if (discountMasterData != null) {

			if (this.getDiscountEndDateIfNull(discountMasterData).after(chargeStartDate.toDate()) && this.getDiscountEndDateIfNull(discountMasterData).before(chargeStartDate.toDate())) {
				isDiscountApplicable = true;	
			}
		}
		
		return isDiscountApplicable;

	}

	// Discount End Date calculation if null
	@SuppressWarnings("deprecation")
	public Date getDiscountEndDateIfNull(DiscountMasterData discountMasterData) {
		Date discountDate = discountMasterData.getDiscountEndDate();
		if (discountMasterData.getDiscountEndDate() == null) {
			discountDate = new Date(2099, 0, 01);
		}
		return discountDate;

	}
	
	// to check price not less than zero
	public BigDecimal chargePriceNotLessThanZero(BigDecimal chargePrice,BigDecimal discountPrice){
		
		chargePrice = chargePrice.subtract(discountPrice);
		if(chargePrice.compareTo(discountPrice) < 0){
			chargePrice = BigDecimal.ZERO;
		}
		return chargePrice;
		
	}
	
	// if is percentage
	public boolean isDiscountPercentage(DiscountMasterData discountMasterData){
		boolean isDiscountPercentage = false;
		if(discountMasterData.getDiscounType().equalsIgnoreCase("percentage")){
			isDiscountPercentage = true;
		}
		return isDiscountPercentage;
	}
	
	// if is discount
	public boolean isDiscountFlat(DiscountMasterData discountMasterData){
		boolean isDiscountFlat = false;
		if(discountMasterData.getDiscounType().equalsIgnoreCase("flat")){
			isDiscountFlat = true;
		}
		return isDiscountFlat;
	}
	

	// Discount calculation 
	public DiscountMasterData calculateDiscount(DiscountMasterData discountMasterData,BigDecimal discountAmount,BigDecimal chargePrice){
		if(isDiscountPercentage(discountMasterData)){
			
			 
			discountAmount = this.calculateDiscountPercentage(discountMasterData.getdiscountRate(), chargePrice);
			discountMasterData.setDiscountAmount(discountAmount);
			chargePrice = this.chargePriceNotLessThanZero(chargePrice, discountAmount);
			discountMasterData.setDiscountedChargeAmount(chargePrice);
			
		}
		
		if(isDiscountFlat(discountMasterData)){
							
			chargePrice = this.calculateDiscountFlat(discountMasterData.getdiscountRate(), chargePrice);
			discountMasterData.setDiscountedChargeAmount(chargePrice);
		}
		return discountMasterData;
	
	}
	
	// Dicount Percent calculation
	public BigDecimal calculateDiscountPercentage(BigDecimal discountRate,BigDecimal chargePrice){
		
		return chargePrice.multiply(discountRate.divide(new BigDecimal(100)));
	}
	
	// Discount Flat calculation
	public BigDecimal calculateDiscountFlat(BigDecimal discountRate,BigDecimal chargePrice){
		
		return chargePrice.subtract(discountRate);
	}
	
	// create billing order command
	public BillingOrderCommand createBillingOrderCommand(BillingOrderData billingOrderData,LocalDate chargeStartDate,
			LocalDate chargeEndDate,LocalDate invoiceTillDate,LocalDate nextBillableDate,BigDecimal price,List<InvoiceTaxCommand> listOfTaxes,DiscountMasterData discountMasterData){
		
		return new BillingOrderCommand(
				billingOrderData.getClientOrderId(),
				billingOrderData.getOderPriceId(),
				billingOrderData.getClientId(), chargeStartDate.toDate(),
				nextBillableDate.toDate(), chargeEndDate.toDate(),
				billingOrderData.getBillingFrequency(),
				billingOrderData.getChargeCode(),
				billingOrderData.getChargeType(),
				billingOrderData.getChargeDuration(),
				billingOrderData.getDurationType(), invoiceTillDate.toDate(), price,
				billingOrderData.getBillingAlign(), listOfTaxes,
				billingOrderData.getStartDate(), billingOrderData.getEndDate(),discountMasterData,billingOrderData.getTaxInclusive());
	}
	
public String roundingDecimal(){
	
	  final String makerCheckerConfigurationProperty = "Rounding";
      final GlobalConfigurationProperty property = this.globalConfigurationRepository.findOneByName(makerCheckerConfigurationProperty);
      if (property == null) { throw new GlobalConfigurationPropertyNotFoundException(makerCheckerConfigurationProperty); }
      
      return property.getValue();
}
	
}
