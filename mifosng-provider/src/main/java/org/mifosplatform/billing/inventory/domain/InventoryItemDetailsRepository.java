package org.mifosplatform.billing.inventory.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InventoryItemDetailsRepository extends JpaRepository<InventoryItemDetails, Long>, JpaSpecificationExecutor<InventoryItemDetails>{

}


