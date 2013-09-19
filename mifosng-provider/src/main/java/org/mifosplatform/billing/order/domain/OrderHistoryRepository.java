package org.mifosplatform.billing.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory,Long>,
JpaSpecificationExecutor<OrderHistory>{

}
