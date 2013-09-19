package org.mifosplatform.billing.billingorder.data;

import java.math.BigDecimal;

import org.joda.time.LocalDate;




public class BillingData  {
private String id;
private String clientId;
private String addrNo;
private String clientName;
private String billPeriod;
private String street;
private String zipcode;
private String city;
private String state;
private String country;
private String previousBalance;
private String chargeAmount;
private String adjustmentAmount;
private String taxAmount;
private String payments;
private String dueAmount;
private String billDate;
private String dueDate;
private String promotionalMessage;
private String billNo;
private String date;
private String transaction;
private String amount;
	public BillingData(Long id, Long clientId, String addrNo, String clientName,
			String billPeriod, String street, String zipcode, String city,
			String state, String country, Double previousBal,
			Double chargeAmount, Double adjustmentAmount, Double taxAmount,
			Double paidAmount, Double adjustAndPayment,LocalDate billDate,LocalDate dueDate,String message) {

		this.billNo=id.toString();
		this.addrNo=addrNo;
		this.adjustmentAmount=adjustmentAmount.toString();
		this.billPeriod=billPeriod;
		this.chargeAmount=chargeAmount.toString();
		this.city=city;
		this.clientId=clientId.toString();
		this.clientName=clientName;
		this.country=country;
		this.dueAmount=adjustAndPayment.toString();
		this.payments=paidAmount.toString();
		this.previousBalance=previousBal.toString();
		this.state=state;
		this.street=street;
		this.taxAmount=taxAmount.toString();
		this.zipcode=zipcode;
		this.billDate=billDate.toString();
		this.dueDate=dueDate.toString();
		this.promotionalMessage=message;

	}

	public BillingData(String id, String clientId, String addrNo,
			String clientName, String billPeriod, String street, String zip,
			String city, String state, String country,
			String previousBalance, String adjustmentAmount,
			String chargeAmount, String taxAmount, String paidAmount,
			String dueAmount, String billDate, String dueDate, String message) {
		this.id=id;
		this.addrNo=addrNo;
		this.adjustmentAmount=adjustmentAmount;
		this.billPeriod=billPeriod;
		this.chargeAmount=chargeAmount;
		this.city=city;
		this.clientId=clientId;
		this.clientName=clientName;
		this.country=country;
		this.dueAmount=dueAmount;
		this.payments=paidAmount;
		this.previousBalance=previousBalance;
		this.state=state;
		this.street=street;
		this.taxAmount=taxAmount;
		this.zipcode=zip;
		this.billDate=billDate;
		this.dueDate=dueDate;
		this.promotionalMessage=message;
	}

	public BillingData(Long transactionId, String transactionType,
			LocalDate transDate, BigDecimal amount) {
		this.id=transactionId.toString();
		this.transaction=transactionType;
		this.date=transDate.toString();
		this.amount=amount.toEngineeringString();
		
	}

	public String getId() {
		return id;
	}

	public String getClientId() {
		return clientId;
	}

	public String getAddrNo() {
		if(addrNo!=null){
		return addrNo;}
		else{
			return "";}
	}

	public String getClientName() {
		if(clientName!=null){
		return clientName;}
		else{
			return "";}
	}

	public String getBillPeriod() {
		return billPeriod;
	}

	public String getStreet() {
		if(street!=null){
		return street;}
		else{
			return "";}
	}

	public String getZip() {
		if(zipcode!=null){
		return zipcode;}
		else{
			return "";}
	}

	public String getCity() {
		if(city!=null){
		return city;}
		else{
			return "";}
	}

	public String getState() {
		if(state!=null){
		return state;}
		else{
			return "";}
	}

	public String getCountry() {
		if(country!=null){
		return country;}
		else{
			return "";}
	}

	public String getPreviousBalance() {
		return previousBalance;
	}

	public String getZipcode() {
		if(zipcode!=null){
		return zipcode;}
		else{
			return "";}
	}

	

	public String getPromotionalMessage() {
		return promotionalMessage;
	}

	public String getBillNo() {
		return billNo;
	}

	public String getDate() {
		return date;
	}

	public String getTransaction() {
		return transaction;
	}

	public String getAmount() {
		return amount;
	}

	public String getPayments() {
		return payments;
	}

	public String getChargeAmount() {
		return chargeAmount;
	}

	public String getAdjustmentAmount() {
		return adjustmentAmount;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	
	public String getDueAmount() {
		return dueAmount;
	}

	public String getBillDate() {
		return billDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public String getMessage() {
		return promotionalMessage;
	}


}
