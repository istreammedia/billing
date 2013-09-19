package org.mifosplatform.billing.contract.domain;


import org.mifosplatform.billing.contract.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriptionRepository  extends
JpaRepository<Contract, Long>,
JpaSpecificationExecutor<Contract>{


}
