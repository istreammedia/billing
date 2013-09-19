package org.mifosplatform.billing.pricing.domain;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.pricing.exceptions.ChargeCOdeExists;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_plan_pricing")
public class Price extends AbstractPersistable<Long> {

	@Column(name = "plan_id")
	private Long planCode;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "charge_code")
	private String chargeCode;

	@Column(name = "charging_variant")
	private String chargingVariant;

	@Column(name = "price", scale = 6, precision = 19, nullable = false)
	private BigDecimal price;

	@Column(name = "discount_id", nullable = false)
	private Long discountId;
	
	@Column(name = "price_region_id", nullable = false)
	private Long priceRegion;

	@Column(name = "is_deleted")
	private String isDeleted="n";

	public Price() {
	}

	public Price(final Long planCode, final String chargeCode,
			final String serviceCode, final String chargingVariant,
			final BigDecimal price, final Long discountId, Long priceregion)

	{

		this.planCode = planCode;
		this.serviceCode = serviceCode;
		this.chargeCode = chargeCode;
		this.chargingVariant = chargingVariant;
		this.price = price;
		this.discountId = discountId;
		this.priceRegion=priceregion;
	}

	public String getChargingVariant() {
		return chargingVariant;
	}

	public void setCharging_variant(String chargingVariant) {
		this.chargingVariant = chargingVariant;
	}

	public Long getPlanCode() {
		return planCode;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Long getDiscountId() {
		return discountId;
	}

	public Long getPriceRegion() {
		return priceRegion;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void delete() {



			this.isDeleted = "y";


	}

	public static Price fromJson(JsonCommand command,
			List<ServiceData> serviceData, Long planId) {
		//  final Long planCode = command.longValueOfParameterNamed("planCode");
		    final String serviceCode = command.stringValueOfParameterNamed("serviceCode");
		    final String chargeCode=command.stringValueOfParameterNamed("chargeCode");
		    final String chargevariant = command.stringValueOfParameterNamed("chargevariant");
		    final Long discountId = command.longValueOfParameterNamed("discountId");
		    final Long priceregion = command.longValueOfParameterNamed("priceregion");
		    final BigDecimal price=command.bigDecimalValueOfParameterNamed("price");
		/* for (ServiceData data : serviceData) {
				if (data.getChargeCode() != null) {
					if ((data.getPlanId() == planId))
						if(data.getServiceCode().equalsIgnoreCase(serviceCode)){
						if(data.getChargeCode().equalsIgnoreCase(chargeCode)) {
						throw new ChargeCOdeExists(data.getChargeDescription());
					}
				}
			}
		}*/
		return new Price(planId, chargeCode, serviceCode,chargevariant, price, discountId,priceregion);
		 
	}

	public  Map<String, Object> update(JsonCommand command) {
		
		 final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);
		  final String firstnameParamName = "serviceCode";
	        if (command.isChangeInStringParameterNamed(firstnameParamName,this.serviceCode)) {
	            final String newValue = command.stringValueOfParameterNamed(firstnameParamName);
	            actualChanges.put(firstnameParamName, newValue);
	            this.serviceCode = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        
	        final String chargeCodeParamName = "chargeCode";
	        if (command.isChangeInStringParameterNamed(chargeCodeParamName, this.chargeCode)) {
	            final String newValue = command.stringValueOfParameterNamed(chargeCodeParamName);
	            actualChanges.put(firstnameParamName, newValue);
	            this.chargeCode = StringUtils.defaultIfEmpty(newValue, null);
	        }
	        final String chargingVariantParamName = "chargingVariant";
			if (command.isChangeInStringParameterNamed(chargingVariantParamName,this.chargingVariant)) {
				final String newValue = command.stringValueOfParameterNamed(chargingVariantParamName);
				actualChanges.put(chargingVariantParamName, newValue);
				this.chargingVariant=StringUtils.defaultIfEmpty(newValue,null);
			}
			
			 final String discountIdParamName = "discountId";
				if (command.isChangeInLongParameterNamed(discountIdParamName,this.discountId)) {
					final Long newValue = command.longValueOfParameterNamed(discountIdParamName);
					actualChanges.put(discountIdParamName, newValue);
					this.discountId=newValue;
				}
				
				 final String priceregionParamName = "priceregion";
					if (command.isChangeInLongParameterNamed(priceregionParamName,this.priceRegion)) {
						final Long newValue = command.longValueOfParameterNamed(priceregionParamName);
						actualChanges.put(priceregionParamName, newValue);
						this.priceRegion=newValue;
					}
				
				 final String priceParamName = "price";
					if (command.isChangeInBigDecimalParameterNamed(priceParamName,this.price)) {
						final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(priceParamName);
						actualChanges.put(priceParamName, newValue);
						this.price=newValue;
					}
	        
	        return actualChanges;

	}
}
