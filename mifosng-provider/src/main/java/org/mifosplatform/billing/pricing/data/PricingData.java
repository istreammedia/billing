package org.mifosplatform.billing.pricing.data;
import java.math.BigDecimal;
import java.util.List;

import org.mifosplatform.billing.charge.data.ChargesData;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.priceregion.data.PriceRegionData;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;


public class PricingData {

	final List<ServiceData> serviceData;
	final List<ChargesData> chargeData;
	final List<EnumOptionData> chargevariant;
	final List<DiscountMasterData> discountdata;
	String planCode;
	private Long planId;
	private Long serviceId;
	private Long chargeId;
	private BigDecimal price;
	private Long discountId;
	private int chargeVariantId;
	private Long id;
	private List<PriceRegionData> priceRegionData;
	private Long priceregion;

	public PricingData(final List<ServiceData> serviceData,	final List<ChargesData> chargeData,
	final List<EnumOptionData> chargevariant,List<DiscountMasterData> data,final String planCode,Long planId,PricingData pricingData, List<PriceRegionData> priceRegionData)
	{

		if(pricingData!= null)
		{
		this.chargeId=pricingData.getChargeId();
		this.serviceId=pricingData.getServiceId();
		this.price=pricingData.getPrice();
		this.discountId=pricingData.getDiscountId();
		this.chargeVariantId=pricingData.getChargeVariantId();
		this.priceregion=pricingData.getPriceregion();
		this.planCode=pricingData.getPlanCode();
		
		}
		this.chargeData=chargeData;
		this.serviceData=serviceData;
		this.chargevariant=chargevariant;
		this.discountdata=data;
		if(planCode!=null){
		this.planCode=planCode;
		}
		this.planId=planId;
		this.priceRegionData=priceRegionData;

	}

	public PricingData(List<ServiceData> serviceData) {
		this.chargeData=null;
		this.serviceData=serviceData;
		this.chargevariant=null;
		this.discountdata=null;
		this.planCode=null;
	}

	public PricingData(Long id, Long serviceId, Long chargeId,
			BigDecimal price, Long discountId, int chargeVariantId, Long priceregion,String planCode) {
	    this.serviceData=null;
		this.chargeData=null;
		this.chargevariant=null;
		this.discountdata=null;
		this.planId=id;
		this.serviceId=serviceId;
		this.chargeId=chargeId;
		this.price=price;
		this.chargeVariantId=chargeVariantId;
		this.discountId=discountId;
		this.priceregion=priceregion;
		this.planCode=planCode;

	}

	public List<ServiceData> getServiceData() {
		return serviceData;
	}

	public List<ChargesData> getChargeData() {
		return chargeData;
	}

	public List<EnumOptionData> getChargevariant() {
		return chargevariant;
	}

	public List<DiscountMasterData> getDiscountdata() {
		return discountdata;
	}

	public Long getPlanId() {
		return planId;
	}

	public String getPlanCode() {
		return planCode;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public Long getChargeId() {
		return chargeId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Long getDiscountId() {
		return discountId;
	}

	public int getChargeVariantId() {
		return chargeVariantId;
	}

	public Long getId() {
		return id;
	}

	public List<PriceRegionData> getPriceRegionData() {
		return priceRegionData;
	}

	public Long getPriceregion() {
		return priceregion;
	}



}
