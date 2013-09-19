package org.mifosplatform.billing.processrequest.service;

import java.util.List;

import org.mifosplatform.billing.processrequest.data.ProcessingDetailsData;

public interface ProcessRequestReadplatformService {

	List<ProcessingDetailsData> retrieveProcessingDetails();

}
