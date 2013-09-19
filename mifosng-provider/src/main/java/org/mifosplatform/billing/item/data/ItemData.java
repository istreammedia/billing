package org.mifosplatform.billing.item.data;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.item.data.ChargesData;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class ItemData {
	
	private Long id;
	private String itemCode;
	private String units;
	private String chargeCode;
	private BigDecimal unitPrice;
	private BigDecimal totalPrice;
	private List<ItemData> itemDatas;
	private ItemData itemData;
	private Integer quantity;
	private List<EnumOptionData> itemClassData;
	private List<EnumOptionData> unitData;
	private List<ChargesData> chargesData;
	private String itemDescription;
	private int warranty;
	private String itemClass;
	private LocalDate saleDate;
	private List<DiscountMasterData> discountMasterDatas;
	

	public ItemData(Long id, String itemCode, String itemDesc,String itemClass,String units,   String chargeCode, int warranty, BigDecimal unitPrice) {
		
		this.id=id;
		this.itemCode=itemCode;
		this.units=units;
		this.unitPrice=unitPrice;
		this.chargeCode=chargeCode;
		this.itemDescription=itemDesc;
		this.warranty=warranty;
		this.itemClass=itemClass;
		
		
		
	}

	public ItemData(List<ItemData> itemCodeData, ItemData itemData, BigDecimal totalPrice,Integer quantity, List<DiscountMasterData> discountdata) {
		this.itemDatas=itemCodeData;
		this.id=itemData.getId();
		this.itemCode=itemData.getItemCode();
		this.chargeCode=itemData.getChargeCode();
		this.units=itemData.getUnits();
		this.unitPrice=itemData.getUnitPrice();
		this.totalPrice=totalPrice;
		this.quantity=quantity;
		this.saleDate=new LocalDate();
		this.discountMasterDatas=discountdata;
		
	
	}

	public ItemData(List<EnumOptionData> itemClassdata,
			List<EnumOptionData> unitTypeData, List<ChargesData> chargeDatas) {
     this.itemClassData=itemClassdata;
     this.unitData=unitTypeData;
     this.chargesData=chargeDatas;
		
		
	}

	

	

	public ItemData(ItemData itemData, List<EnumOptionData> itemClassdata,
			List<EnumOptionData> unitTypeData, List<ChargesData> chargeDatas) {
		this.id=itemData.getId();
		this.itemCode=itemData.getItemCode();
		this.units=itemData.getUnits();
		this.unitPrice=itemData.getUnitPrice();
		this.chargeCode=itemData.getChargeCode();
		this.itemDescription=itemData.getItemDescription();
		this.warranty=itemData.getWarranty();
		this.itemClass=itemData.getItemClass();
		this.chargesData=chargeDatas;
		this.unitData=unitTypeData;
		this.itemClassData=itemClassdata;
	}

	public ItemData(List<ItemData> itemCodes) {
		this.itemDatas = itemCodes;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public List<ItemData> getItemDatas() {
		return itemDatas;
	}

	public String getItemClass() {
		return itemClass;
	}

	public ItemData getItemData() {
		return itemData;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public List<EnumOptionData> getItemClassData() {
		return itemClassData;
	}

	public List<EnumOptionData> getUnitData() {
		return unitData;
	}

	public List<ChargesData> getChargesData() {
		return chargesData;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public int getWarranty() {
		return warranty;
	}

	public Long getId() {
		return id;
	}

	public String getItemCode() {
		return itemCode;
	}

	public String getUnits() {
		return units;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void set(BigDecimal totalPrice) {
	this.totalPrice=totalPrice;
		
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public List<ItemData> getItemCodeData() {
		return 	itemDatas;
	}

	
}
