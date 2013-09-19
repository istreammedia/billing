package org.mifosplatform.billing.selfcare.service;

import org.mifosplatform.billing.selfcare.domain.SelfCare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SelfCareRepository extends JpaRepository<SelfCare, Long>, JpaSpecificationExecutor<SelfCare>{

}
