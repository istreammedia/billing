package org.mifosplatform.billing.clientprospect.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProspectDetailJpaRepository extends JpaRepository<ProspectDetail, Long>,JpaSpecificationExecutor<ProspectDetail> {

}
