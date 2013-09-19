package org.mifosplatform.billing.onetimesale.data;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.charge.data.ChargesData;
import org.mifosplatform.billing.eventorder.data.EventOrderData;
import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.service.DiscountMasterData;

public class OneTimeSaleData {
	
	private List<ChargesData> chargesDatas;
	private List<ItemData> itemDatas;
	private Long itemId;
	private Long id;
	private String units;
	private String itemCode;
	private String chargeCode;
	private String quantity;
	private BigDecimal unitPrice;
	private BigDecimal totalPrice;
	private LocalDate saleDate;
	private Long clientId;
	private String isInvoiced;
	private OneTimeSaleData salesData;
	private boolean flag = false;
	private String hardwareAllocated;
	private String itemClass;
	private List<AllocationDetailsData> allocationData;
	private List<DiscountMasterData> discountMasterDatas;
	private Long discountId;
	private List<OneTimeSaleData> oneTimeSaleData;
	private List<EventOrderData> eventOrdersData;
	
	
	public OneTimeSaleData(List<ChargesData> chargeDatas,List<ItemData> itemData, OneTimeSaleData salesData, List<DiscountMasterData> discountdata) {
		
		this.chargesDatas=chargeDatas;
		this.itemDatas=itemData;
		this.saleDate=new LocalDate();
		this.salesData=salesData;
		this.discountMasterDatas=discountdata;
		
	}

	/*hardware allocated and flag is added by rahman */

	public OneTimeSaleData(Long id, LocalDate saleDate, String itemCode,
			String chargeCode, String quantity, BigDecimal totalPrice,String hardwareAllocated,String itemClass) {
		this.id=id;
		this.saleDate=saleDate;
		this.itemCode=itemCode;
		this.chargeCode=chargeCode;
		this.quantity=quantity;
		this.totalPrice=totalPrice;
		this.hardwareAllocated = hardwareAllocated;
		this.flag = hardwareAllocated.equalsIgnoreCase("ALLOCATED")?true:false;
		this.itemClass = itemClass;
		
	}


	public OneTimeSaleData(Long oneTimeSaleId, Long clientId, String units,
			String chargeCode, BigDecimal unitPrice, String quantity,
			BigDecimal totalPrice, String isInvoiced, Long itemId, Long discountId) {
		this.id = oneTimeSaleId;
		this.clientId = clientId;
		this.units = units;
		this.chargeCode = chargeCode;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.isInvoiced = isInvoiced;
		this.itemId = itemId;
		this.discountId =discountId;
	}


	public OneTimeSaleData() {
		// TODO Auto-generated constructor stub
	}

	public OneTimeSaleData(List<OneTimeSaleData> salesData,List<EventOrderData> eventOrderDatas) {
             
		this.oneTimeSaleData=salesData;
		this.eventOrdersData=eventOrderDatas;
	
	
	}

	public List<ChargesData> getChargesDatas() {
		return chargesDatas;
	}


	public List<ItemData> getItemDatas() {
		return itemDatas;
	}


	public Long getItemId() {
		return itemId;
	}
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getUnits() {
		return units;
	}


	public String getChargeCode() {
		return chargeCode;
	}


	public String getQuantity() {
		return quantity;
	}


	public BigDecimal getUnitPrice() {
		return unitPrice;
	}


	public BigDecimal getTotalPrice() {
		return totalPrice;
	}


	public LocalDate getSaleDate() {
		return saleDate;
	}


	public String getIsInvoiced() {
		return isInvoiced;
	}


	public void setIsInvoiced(String isInvoiced) {
		this.isInvoiced = isInvoiced;
	}


	public Long getClientId() {
		return clientId;
	}


	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	
	public String getHardwareAllocated() {
		return hardwareAllocated;
	}

	public void setHardwareAllocated(String hardwareAllocated) {
		this.hardwareAllocated = hardwareAllocated;
	}

	public void setAllocationDetails(List<AllocationDetailsData> data) {
		this.allocationData=data;
		
	}

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @return the salesData
	 */
	public OneTimeSaleData getSalesData() {
		return salesData;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @return the itemClass
	 */
	public String getItemClass() {
		return itemClass;
	}

	/**
	 * @return the allocationData
	 */
	public List<AllocationDetailsData> getAllocationData() {
		return allocationData;
	}

	/**
	 * @return the discountMasterDatas
	 */
	public List<DiscountMasterData> getDiscountMasterDatas() {
		return discountMasterDatas;
	}

	/**
	 * @return the discountId
	 */
	public Long getDiscountId() {
		return discountId;
	}

	

}
