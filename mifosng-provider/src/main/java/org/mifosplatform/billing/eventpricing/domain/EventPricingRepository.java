/**
 * 
 */
package org.mifosplatform.billing.eventpricing.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Interface for {@link EventPricing} Repository
 * extends {@link JpaRepository} and {@link JpaSpecificationExecutor}
 * 
 * @author pavani
 *
 */
public interface EventPricingRepository extends
		JpaRepository<EventPricing, Long>,
		JpaSpecificationExecutor<EventPricing> {

}
