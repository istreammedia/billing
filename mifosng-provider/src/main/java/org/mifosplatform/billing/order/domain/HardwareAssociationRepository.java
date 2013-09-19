package org.mifosplatform.billing.order.domain;

import org.mifosplatform.billing.association.domain.HardwareAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HardwareAssociationRepository  extends
JpaRepository<HardwareAssociation, Long>,
JpaSpecificationExecutor<HardwareAssociation>{

}
