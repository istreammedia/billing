package org.mifosplatform.billing.mediadetails.service;

import java.util.List;

import org.mifosplatform.billing.eventpricing.data.EventPricingData;
import org.mifosplatform.billing.mediadetails.data.MediaAssetDetailsData;
import org.mifosplatform.billing.mediadetails.data.MediaAssetLocationDetails;
import org.mifosplatform.billing.mediadetails.data.MediaLocationData;

public interface MediaAssetDetailsReadPlatformService {


	List<String> retrieveGenresData(Long mediaId);

	List<String> retrieveProductions(Long mediaId);

	List<MediaLocationData> retrieveFilmLocation(Long mediaId);

	List<String> retrieveWriters(Long mediaId);

	List<String> retrieveDirectors(Long mediaId);

	List<String> retrieveActors(Long mediaId);

	List<String> retrieveCountry(Long mediaId);
	MediaAssetDetailsData retrieveMediaAssetDetailsData(Long category);
	
	List<MediaAssetLocationDetails> retrieveMediaAssetLocationData(Long mediaId);

	List<EventPricingData> getEventPriceDetails(Long eventId, String clientType);

}
