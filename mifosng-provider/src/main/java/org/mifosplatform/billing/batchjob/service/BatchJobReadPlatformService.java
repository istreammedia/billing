package org.mifosplatform.billing.batchjob.service;

import java.util.List;

import org.mifosplatform.billing.batchjob.data.BatchJobData;

public interface BatchJobReadPlatformService {
	
	public List<BatchJobData> retriveBatchDetails();
}
