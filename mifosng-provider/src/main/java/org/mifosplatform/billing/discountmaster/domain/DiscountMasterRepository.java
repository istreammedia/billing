package org.mifosplatform.billing.discountmaster.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DiscountMasterRepository extends

JpaRepository<DiscountMaster, Long>,
JpaSpecificationExecutor<DiscountMaster>{

}
