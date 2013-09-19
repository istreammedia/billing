package org.mifosplatform.billing.inventory.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;



@Entity
@Table(name="b_allocation")
public class InventoryItemDetailsAllocation extends AbstractAuditableCustom<AppUser,Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*order_id is nothing but a ID of b_onetime_sale table*/
	
	@Column(name="order_id",nullable=false,length=20)
	private Long orderId;
	
	@Column(name="client_id",nullable=false,length=20)
	private Long clientId; 
	
	@Column(name="item_master_id",nullable=false,length=20)
	private Long itemMasterId;
	
	@Column(name="serial_no",nullable=false,length=100)
	private String serialNumber;
	
	@Column(name="allocation_date",nullable=true)
	private Date allocationDate;
	
	@Column(name="status",nullable=true)
	private String status;

	public InventoryItemDetailsAllocation(){}

	public InventoryItemDetailsAllocation(final Long orderId,final Long clientId,final Long itemMasterId,final String serialNumber,Date allocationDate,String status){
		this.orderId = orderId;
		this.clientId = clientId;
		this.itemMasterId = itemMasterId;
		this.serialNumber = serialNumber;
		this.allocationDate = allocationDate;
		this.status = status;
	}
	
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getItemMasterId() {
		return itemMasterId;
	}

	public void setItemMasterId(Long itemMasterId) {
		this.itemMasterId = itemMasterId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(Date allocationDate) {
		this.allocationDate = allocationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static InventoryItemDetailsAllocation fromJson(JsonCommand command) {
		Long orderId = command.longValueOfParameterNamed("orderId");
		Long clientId = command.longValueOfParameterNamed("clientId");
		Long itemMasterId = command.longValueOfParameterNamed("itemMasterId");
		String serialNumber = command.stringValueOfParameterNamed("serialNumber");
		Date allocationDate = new Date();
		String status = command.stringValueOfParameterNamed("status");
		return new InventoryItemDetailsAllocation(orderId,clientId,itemMasterId,serialNumber,allocationDate,status);
	}
	
	
}
