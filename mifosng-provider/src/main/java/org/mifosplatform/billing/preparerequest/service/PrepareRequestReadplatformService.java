package org.mifosplatform.billing.preparerequest.service;

import java.util.List;

import org.mifosplatform.billing.preparerequest.data.PrepareRequestData;





public interface PrepareRequestReadplatformService {

	List<PrepareRequestData> retrieveDataForProcessing();

	List<Long> retrieveRequestClientOrderDetails(Long clientId);

	
	void processingClientDetails(PrepareRequestData requestData);

}
