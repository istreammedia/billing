package org.mifosplatform.billing.pricing.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PriceRepository extends JpaRepository<Price, Long>,
JpaSpecificationExecutor<Price>{




}
