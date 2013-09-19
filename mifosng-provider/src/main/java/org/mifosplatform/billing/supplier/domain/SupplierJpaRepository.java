package org.mifosplatform.billing.supplier.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SupplierJpaRepository extends JpaRepository<Supplier, Long>,
		JpaSpecificationExecutor<Supplier> {

}
