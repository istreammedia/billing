package org.mifosplatform.billing.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.mcodevalues.data.MCodeData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class DiscountMasterData {
	private long id;
	private String discountCode;
	private String discountDescription;
	private String discounType;
	private BigDecimal discountRate;
	private LocalDate discountStartDate;
	private Date discountEndDate;
	private String isDeleted;
	private Long orderPriceId;
	private Long discountMasterId;
	private BigDecimal discountAmount;
	private BigDecimal discountedChargeAmount;
	private List<EnumOptionData> status;
	private Collection<MCodeData> discounTypeData;
	private String discountstatus;
	private BigDecimal discountValue;

	public DiscountMasterData(long id, String discountCode,String discountDescription, String discounType, BigDecimal discountRate,
			LocalDate startDate,String status) {
		this.discountMasterId = id;
		this.discountCode=discountCode;
		this.discountDescription = discountDescription;
		this.discounType = discounType;
		this.discountRate = discountRate;
		this.discountAmount = BigDecimal.ZERO;
		this.discountedChargeAmount = BigDecimal.ZERO;
		this.discountstatus=status;
		this.discountStartDate=startDate;

	}

	/*public DiscountMasterData(Long id, String discountcode, String discountdesc) {
		this.id = id;
		this.discountCode = discountcode;
		this.discountDescription = discountdesc;
		this.discounType = null;
		this.discountMasterId=id;
		this.discountAmount = BigDecimal.ZERO;
		this.discountedChargeAmount = BigDecimal.ZERO;
		// this.discountRate=;
	}*/

	// discount master manoj
	public DiscountMasterData(Long discountMasterid, Long orderPriceId,Long orderDiscountId, LocalDate discountStartDate,Date discountEndDate, 
			String discountType, BigDecimal discountRate,String isDeleted) {
		this.id = discountMasterid;
		this.orderPriceId = orderPriceId;
		this.discountMasterId = orderDiscountId;
		this.discountStartDate = discountStartDate;
		this.discountEndDate = discountEndDate;
		this.discounType = discountType;
		this.discountValue = discountRate;
		this.isDeleted = isDeleted;
		this.discountAmount = BigDecimal.ZERO;
		this.discountedChargeAmount = BigDecimal.ZERO;
	}

	public DiscountMasterData(List<EnumOptionData> status,Collection<MCodeData> discountTypeDate) {
           this.status=status;
           this.discounTypeData=discountTypeDate;
	
	}

	public BigDecimal getDiscountedChargeAmount() {
		return discountedChargeAmount;
	}

	public void setDiscountedChargeAmount(BigDecimal discountedChargeAmount) {
		this.discountedChargeAmount = discountedChargeAmount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public String getDiscountDescription() {
		return discountDescription;
	}

	public void setDiscountDescription(String discountDescription) {
		this.discountDescription = discountDescription;
	}

	public String getDiscounType() {
		return discounType;
	}

	public void setDiscounType(String discounType) {
		this.discounType = discounType;
	}

	public BigDecimal getdiscountRate() {
		return discountRate;
	}

	public void setdiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public LocalDate getDiscountStartDate() {
		return discountStartDate;
	}

	public void setDiscountStartDate(LocalDate discountStartDate) {
		this.discountStartDate = discountStartDate;
	}

	public Date getDiscountEndDate() {
		return discountEndDate;
	}

	public void setDiscountEndDate(Date discountEndDate) {
		this.discountEndDate = discountEndDate;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getOrderPriceId() {
		return orderPriceId;
	}

	public void setOrderPriceId(Long orderPriceId) {
		this.orderPriceId = orderPriceId;
	}

	public Long getDiscountMasterId() {
		return discountMasterId;
	}

	public void setDiscountMasterId(Long discountMasterId) {
		this.discountMasterId = discountMasterId;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public List<EnumOptionData> getStatus() {
		return status;
	}

	public void setStatus(List<EnumOptionData> status) {
		this.status = status;
	}

	public Collection<MCodeData> getDiscounTypeData() {
		return discounTypeData;
	}

	public void setDiscounTypeData(Collection<MCodeData> discounTypeData) {
		this.discounTypeData = discounTypeData;
	}

	public String getDiscountstatus() {
		return discountstatus;
	}

	public void setDiscountstatus(String discountstatus) {
		this.discountstatus = discountstatus;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	
	
	
}
