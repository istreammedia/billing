package org.mifosplatform.billing.scheduledjobs.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScheduledJobRepository extends JpaRepository<ScheduleJobs, Long>,
JpaSpecificationExecutor<ScheduleJobs>{

	 ScheduleJobs findByBatchName(String jobName);

}
