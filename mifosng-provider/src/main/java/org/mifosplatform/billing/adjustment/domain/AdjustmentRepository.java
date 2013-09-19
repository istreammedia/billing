package org.mifosplatform.billing.adjustment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long>, JpaSpecificationExecutor<Adjustment>{

}
