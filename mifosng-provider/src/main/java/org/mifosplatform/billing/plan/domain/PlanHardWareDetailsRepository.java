package org.mifosplatform.billing.plan.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlanHardWareDetailsRepository  extends
JpaRepository<PlanHardWareDetails, Long>,
JpaSpecificationExecutor<PlanHardWareDetails>{

}
