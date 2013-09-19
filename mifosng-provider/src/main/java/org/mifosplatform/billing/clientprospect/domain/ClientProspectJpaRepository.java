package org.mifosplatform.billing.clientprospect.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientProspectJpaRepository extends JpaRepository<ClientProspect, Long>,JpaSpecificationExecutor<ClientProspect>{

}
