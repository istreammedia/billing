package org.mifosplatform.billing.onetimesale.data;

import org.joda.time.LocalDate;

public class AllocationDetailsData {
	
	private final Long id;
	private final String itemDescription;
	private final String serialNo;
	private final LocalDate allocationDate;
	public AllocationDetailsData(Long id, String itemDescription,String serialNo, LocalDate allocationDate) {
		this.id=id;
		this.itemDescription=itemDescription;
		this.serialNo=serialNo;
		this.allocationDate=allocationDate;
	}
	public AllocationDetailsData(Long id, Long orderId, String serialNum,
			Long clientId) {
		
		this.id=id;
		this.serialNo=serialNum;
		this.itemDescription=null;
		this.allocationDate=null;
		
	}
	public Long getId() {
		return id;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public LocalDate getAllocationDate() {
		return allocationDate;
	}

	
}
