package org.mifosplatform.billing.chargecode.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChargeCodeRepository extends JpaRepository<ChargeCode, Long>,
											   JpaSpecificationExecutor<ChargeCode>{

}
