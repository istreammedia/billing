package org.mifosplatform.billing.inventory.mrn.data;

import org.joda.time.LocalDate;

public class InventoryTransactionHistoryData {
	
	private Long id;
	private LocalDate transactionDate;
	private Long mrnId;
	private String serialNumber;
	private String itemDescription;
	private String fromOffice;
	private String toOffice;
	private String refType;
	private String movement;

	
	public InventoryTransactionHistoryData() {
		
	}
	
	public InventoryTransactionHistoryData(final Long id,final LocalDate transactionDate, final Long mrnId, final String itemDescription, final String fromOffice, final String toOffice, final String serialNumber, final String refType){
		this.id = id;
		this.transactionDate = transactionDate;
		this.mrnId = mrnId;
		this.itemDescription = itemDescription;
		this.fromOffice = fromOffice;
		this.toOffice = toOffice;
		this.serialNumber = serialNumber;
		this.refType = refType;
	}
	
	public InventoryTransactionHistoryData(final Long id,final LocalDate transactionDate, final Long mrnId, final String itemDescription, final String fromOffice, final String toOffice, final String serialNumber, final String refType, final String movement){
		this.id = id;
		this.transactionDate = transactionDate;
		this.mrnId = mrnId;
		this.itemDescription = itemDescription;
		this.fromOffice = fromOffice;
		this.toOffice = toOffice;
		this.serialNumber = serialNumber;
		this.refType = refType;
		this.movement = movement;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Long getMrnId() {
		return mrnId;
	}

	public void setMrnId(Long mrnId) {
		this.mrnId = mrnId;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getFromOffice() {
		return fromOffice;
	}

	public void setFromOffice(String fromOffice) {
		this.fromOffice = fromOffice;
	}

	public String getToOffice() {
		return toOffice;
	}

	public void setToOffice(String toOffice) {
		this.toOffice = toOffice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getMovement() {
		return movement;
	}

	public void setMovement(String movement) {
		this.movement = movement;
	}

	
}
