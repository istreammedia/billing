package org.mifosplatform.billing.batchjobscheduling.service;

import java.util.List;

import org.mifosplatform.billing.batchjobscheduling.data.BatchJobSchedulingData;
import org.mifosplatform.billing.batchjobscheduling.data.BatchNameData;
import org.mifosplatform.billing.batchjobscheduling.data.MessageTemplateData;
import org.mifosplatform.billing.batchjobscheduling.data.ProcessTypeData;
import org.mifosplatform.billing.batchjobscheduling.data.ScheduleTypeData;

public interface BatchJobSchedulingReadPlatformService {

	public List<BatchJobSchedulingData> retriveAllData();
	public BatchJobSchedulingData retriveSingleData();
	public List<BatchNameData> retriveBatchNames();
	public List<ProcessTypeData> retriveProcessTypes();
	public List<ScheduleTypeData> retriveScheduleTypes();
	public List<MessageTemplateData> retriveMessageTemplates();
	
}
