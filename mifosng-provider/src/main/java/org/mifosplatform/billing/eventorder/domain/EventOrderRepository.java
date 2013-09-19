package org.mifosplatform.billing.eventorder.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventOrderRepository extends JpaRepository<EventOrder, Long>,
JpaSpecificationExecutor<EventOrder>{

}
