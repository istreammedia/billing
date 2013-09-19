package org.mifosplatform.billing.plan.data;

import java.math.BigDecimal;
import java.util.List;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class ServiceData {

	private final Long id;

	private final String serviceCode;
	private final String planDescription;
	private final String planCode;
	private final List<EnumOptionData> data;
	private final String discountCode;
	private final BigDecimal price;
	private final String chargeCode;
	private final String chargeVariant;
	private final String services;
	private Long serviceId;
	private Long planId;
	private String chargeDescription;
	private String serviceDescription;
	private String code;
    private String serviceType;
	private String priceregion;

	public ServiceData(Long id, String planCode, String serviceCode,
			String planDescription, String chargeCode, String charging_variant,
			BigDecimal price, String priceregion) {

		this.id = id;
		this.discountCode = null;
		this.serviceCode = serviceCode;
		this.planDescription = planDescription;
		this.planCode = planCode;
		this.data = null;
		this.chargeCode = chargeCode;
		this.chargeVariant = charging_variant;
		this.price = price;
		this.services = null;
		this.priceregion=priceregion;

	}

	public ServiceData(Long id, Long planId, String planCode,String chargeCode, String serviceCode, 
			String serviceDescription, String priceRegion) {
		this.id = id;
		this.planId = planId;
		this.discountCode = null;
		this.serviceCode = serviceCode;
		this.planDescription = null;
		this.planCode = planCode;
		this.data = null;
		this.chargeCode = chargeCode;
		this.chargeVariant = null;
		this.price = null;
		this.services = null;
		this.serviceDescription = serviceDescription;
		this.code=null;
		this.priceregion=priceRegion;
		this.chargeDescription=serviceDescription;
	}

	public Long getId() {
		return id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getServiceDescription() {
		return planDescription;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public String getPlanCode() {
		return planCode;
	}
	
	

	public String getPriceregion() {
		return priceregion;
	}

	public String getServices() {
		return services;
	}

	public Long getPlanId() {
		return planId;
	}

	public String getPlanDescription() {
		return planDescription;
	}

	public List<EnumOptionData> getData() {
		return data;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public String getChargeVariant() {
		return chargeVariant;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public String getChargeDescription() {
		return chargeDescription;
	}

	public void setChargeDescription(String chargeDescription) {
		this.chargeDescription = chargeDescription;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getCode() {
		return code;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {

		
		this.serviceType=serviceType;
	}



}
