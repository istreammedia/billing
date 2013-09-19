package org.mifosplatform.billing.billingorder.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientOrderRepository extends  JpaRepository<ClientOrder, Long>,
JpaSpecificationExecutor<ClientOrder>{

}
