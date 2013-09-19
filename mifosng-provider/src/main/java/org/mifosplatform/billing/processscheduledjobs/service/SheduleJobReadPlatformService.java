package org.mifosplatform.billing.processscheduledjobs.service;

import java.util.List;

import org.mifosplatform.billing.scheduledjobs.data.ScheduleJobData;

public interface SheduleJobReadPlatformService {

	List<ScheduleJobData> retrieveSheduleJobDetails();

	List<Long> getClientIds(String query);

	Long getMessageId(String processParam);

}
