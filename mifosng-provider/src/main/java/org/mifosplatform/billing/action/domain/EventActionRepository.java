package org.mifosplatform.billing.action.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventActionRepository extends JpaRepository<EventAction, Long>,
JpaSpecificationExecutor<EventAction>{

}
