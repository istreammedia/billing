package org.mifosplatform.billing.inventory.mrn.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InventoryTransactionHistoryJpaRepository extends
		JpaRepository<InventoryTransactionHistory, Long>, JpaSpecificationExecutor<InventoryTransactionHistory> {

}
