package org.mifosplatform.billing.order.service;

import org.mifosplatform.billing.priceregion.data.PriceRegionData;
import org.mifosplatform.billing.priceregion.service.RegionalPriceReadplatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClientRegionDetails {
	
	private final RegionalPriceReadplatformService regionalPriceReadplatformService;
	public static final String CLIENT_REGION="ALL";
	
	@Autowired
	public ClientRegionDetails(final RegionalPriceReadplatformService regionalPriceReadplatformService) {
		
		this.regionalPriceReadplatformService=regionalPriceReadplatformService;
	}

	
	public String getTheClientRegionDetails(Long clientId) {
		
		try{
			PriceRegionData priceRegionData=this.regionalPriceReadplatformService.getTheClientRegionDetails(clientId);
			if(priceRegionData!=null)
				return priceRegionData.getPriceregion();
			else
				 return CLIENT_REGION;
				
		}catch(Exception e){
			return null;
		}
		
		
	}

}
