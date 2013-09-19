package org.mifosplatform.billing.inventory.mrn.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;

@Entity
@Table(name="b_item_history")
public class InventoryTransactionHistory extends AbstractAuditableCustom<AppUser, Long> {
	   
	@Column(name="transaction_date")
	private Date transactionDate;
	
	@Column(name="ref_id")
	private Long refId;
	
	@Column(name="ref_type")
	private String refType;
	
	@Column(name="serial_number")
	private String serialNumber;
	
	@Column(name="item_master_id")
	private Long itemMasterId;
	
	@Column(name="from_office")
	private Long fromOffice;
	
	@Column(name="to_office")
	private Long toOffice;
	
	public InventoryTransactionHistory() {
		
	}
	
	public InventoryTransactionHistory(final Date transactionDate, final Long refId, final String refType, final String serialNumber, final Long itemMasterId, final Long fromOffice, final Long toOffice){
		
		this.transactionDate = transactionDate;
		this.refId = refId;
		this.refType = refType;
		this.serialNumber = serialNumber;
		this.itemMasterId = itemMasterId;
		this.fromOffice = fromOffice;
		this.toOffice = toOffice;
		
	
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Long getMrnId() {
		return refId;
	}

	public void setMrnId(Long refId) {
		this.refId = refId;
	}

	public Long getItemMasterId() {
		return itemMasterId;
	}

	public void setItemMasterId(Long itemMasterId) {
		this.itemMasterId = itemMasterId;
	}

	public Long getFromOffice() {
		return fromOffice;
	}

	public void setFromOffice(Long fromOffice) {
		this.fromOffice = fromOffice;
	}

	public Long getToOffice() {
		return toOffice;
	}

	public void setToOffice(Long toOffice) {
		this.toOffice = toOffice;
	}


	/*public static InventoryTransactionHistory logTransaction(
			MRNMoveDetailsData mrnMoveDetailsData, MRNDetails mrnDetails,
			List<Long> itemMasterId) {
		
		final Date transactionDate = mrnMoveDetailsData.getMovedDate();
		final Long mrnId = mrnMoveDetailsData.getMrnId();
		final Long iMasterId = itemMasterId.get(0);
		final Long fromOffice = mrnDetails.getFromOffice();
		final Long toOffice = mrnDetails.getToOffice();
		final String serialNumber = mrnMoveDetailsData.getSerialNumber();
		return new InventoryTransactionHistory(transactionDate, mrnId,"MRN", iMasterId, fromOffice, toOffice, serialNumber) ;
	}*/

	public static InventoryTransactionHistory logTransaction(Date transactionDate,
			Long refId, String refType, String serialNumber, Long itemMasterId, Long fromOffice, Long toOffice) {
				
		
		return new InventoryTransactionHistory(transactionDate, refId, refType, serialNumber, itemMasterId, fromOffice, toOffice);
	}
	
	
}
