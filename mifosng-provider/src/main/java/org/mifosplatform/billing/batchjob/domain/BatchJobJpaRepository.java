package org.mifosplatform.billing.batchjob.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BatchJobJpaRepository extends JpaRepository<BatchJob, Long>,JpaSpecificationExecutor<BatchJob>{

}
