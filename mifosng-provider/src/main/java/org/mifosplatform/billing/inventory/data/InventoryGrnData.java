package org.mifosplatform.billing.inventory.data;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.supplier.data.SupplierData;
import org.mifosplatform.organisation.office.data.OfficeData;

public class InventoryGrnData {


	
	
	
	private Long id;
	private LocalDate purchaseDate;
	private Long supplierId;
	private Long itemMasterId;
	private Long orderdQuantity;
	private Long receivedQuantity;
	private Long createdById;
	private Date createdDate;
	private Date lastModifiedDate;
	private Long lastModifiedById;
	private Long testId;
	private Long balanceQuantity;
	
	private List<ItemData> itemData;
	private Collection<OfficeData> officeData;	
	private List<SupplierData> supplierData;
	private String itemDescription;
	private String supplierName;
	private String officeName;
	
	
	
	public InventoryGrnData(){
		this.id=null;
		this.itemMasterId=null;
		this.orderdQuantity=null;
		this.purchaseDate= new LocalDate();
		this.receivedQuantity=null;
		this.supplierId=null;
		
	}
	
	public InventoryGrnData(Long testId, final String itemDescription) {
		this.testId = testId;
		this.itemDescription = itemDescription;
	}
	
	
	
	public InventoryGrnData(Long id,LocalDate purchaseDate,Long supplierId,Long itemMasterId,Long orderedQuantity,Long receivedQuantity){
		this.id=id;
		this.itemMasterId=itemMasterId;
		this.orderdQuantity=orderedQuantity;
		this.purchaseDate=purchaseDate;
		this.receivedQuantity=receivedQuantity;
		this.supplierId=supplierId;		
		this.balanceQuantity = orderedQuantity-receivedQuantity;
	}
	
	public InventoryGrnData(Long id,LocalDate purchaseDate,Long supplierId,Long itemMasterId,Long orderedQuantity,Long receivedQuantity,String itemDescription, String supplierName){
		this.id=id;
		this.itemMasterId=itemMasterId;
		this.orderdQuantity=orderedQuantity;
		this.purchaseDate=purchaseDate;
		this.receivedQuantity=receivedQuantity;
		this.supplierId=supplierId;		
		this.balanceQuantity = orderedQuantity-receivedQuantity;
		this.itemDescription = itemDescription;
		this.supplierName = supplierName;
	}
	
	
	public InventoryGrnData(final Long id,final LocalDate purchaseDate,final Long supplierId,final Long itemMasterId,final Long orderedQuantity,final Long receivedQuantity,final String itemDescription, final String supplierName,final String officeName){
		this.id=id;
		this.itemMasterId=itemMasterId;
		this.orderdQuantity=orderedQuantity;
		this.purchaseDate=purchaseDate;
		this.receivedQuantity=receivedQuantity;
		this.supplierId=supplierId;		
		this.balanceQuantity = orderedQuantity-receivedQuantity;
		this.itemDescription = itemDescription;
		this.supplierName = supplierName;
		this.officeName = officeName;
	}
	
	public InventoryGrnData(List<ItemData> itemData,Collection<OfficeData> officeData, List<SupplierData> supplierData) {
		this.itemData  = itemData;
		this.officeData = officeData;
		this.supplierData = supplierData;
	}
	
	public InventoryGrnData(Long testId) {
		this.testId = testId;
	}
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getLastModifiedById() {
		return lastModifiedById;
	}

	public void setLastModifiedById(Long lastModifiedById) {
		this.lastModifiedById = lastModifiedById;
	}

	public Long getItemMasterId() {
		return itemMasterId;
	}

	public void setItemMasterId(Long itemMasterId) {
		this.itemMasterId = itemMasterId;
	}

	public Long getOrderdQuantity() {
		return orderdQuantity;
	}

	public void setOrderdQuantity(Long orderdQuantity) {
		this.orderdQuantity = orderdQuantity;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Long getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Long receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	/**
	 * @return the itemData
	 */
	public List<ItemData> getItemData() {
		return itemData;
	}

	/**
	 * @param itemData the itemData to set
	 */
	public void setItemData(List<ItemData> itemData) {
		this.itemData = itemData;
	}

	/**
	 * @return the officeData
	 */
	public Collection<OfficeData> getOfficeData() {
		return officeData;
	}

	/**
	 * @param officeData the officeData to set
	 */
	public void setOfficeData(Collection<OfficeData> officeData) {
		this.officeData = officeData;
	}

	/**
	 * @return the supplierData
	 */
	public List<SupplierData> getSupplierData() {
		return supplierData;
	}

	/**
	 * @param supplierData the supplierData to set
	 */
	public void setSupplierData(List<SupplierData> supplierData) {
		this.supplierData = supplierData;
	}

	/**
	 * @return the balanceQuantity
	 */
	public Long getBalanceQuantity() {
		return balanceQuantity;
	}

	/**
	 * @param balanceQuantity the balanceQuantity to set
	 */
	public void setBalanceQuantity(Long balanceQuantity) {
		this.balanceQuantity = balanceQuantity;
	}

	/**
	 * @return the itemDescription
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * @param itemDescription the itemDescription to set
	 */
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	/**
	 * @return the supplierName
	 */
	public String getSupplierName() {
		return supplierName;
	}

	/**
	 * @param supplierName the supplierName to set
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	
	
}
