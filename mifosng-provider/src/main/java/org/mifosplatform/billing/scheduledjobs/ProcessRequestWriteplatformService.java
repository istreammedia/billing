package org.mifosplatform.billing.scheduledjobs;

import org.mifosplatform.billing.processrequest.data.ProcessingDetailsData;

public interface ProcessRequestWriteplatformService {

	void ProcessingRequestDetails();

	void notifyProcessingDetails(ProcessingDetailsData detailsData);

}
