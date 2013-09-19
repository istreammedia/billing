package org.mifosplatform.billing.inventory.mrn.api;

import org.mifosplatform.billing.inventory.mrn.domain.MRNDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MRNDetailsJpaRepository extends JpaRepository<MRNDetails, Long>,
		JpaSpecificationExecutor<MRNDetails> {

}
