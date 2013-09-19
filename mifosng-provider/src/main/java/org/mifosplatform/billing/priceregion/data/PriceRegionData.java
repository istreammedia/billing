package org.mifosplatform.billing.priceregion.data;

public class PriceRegionData {
	
	private final Long id;
	private final String priceregionCode;
	private final String priceregion;

	public PriceRegionData(Long id, String priceCode, String priceRegion) {
             this.id=id;
             this.priceregion=priceRegion;
             this.priceregionCode=priceCode;
	
	}

	public Long getId() {
		return id;
	}

	public String getPriceregion() {
		return priceregion;
	}

	public String getPriceregionCode() {
		return priceregionCode;
	}

}
