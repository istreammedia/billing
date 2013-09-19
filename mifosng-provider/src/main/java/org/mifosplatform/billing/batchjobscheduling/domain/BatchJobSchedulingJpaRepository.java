package org.mifosplatform.billing.batchjobscheduling.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BatchJobSchedulingJpaRepository extends JpaRepository<BatchJobScheduling, Long>,JpaSpecificationExecutor<BatchJobScheduling> {

}
