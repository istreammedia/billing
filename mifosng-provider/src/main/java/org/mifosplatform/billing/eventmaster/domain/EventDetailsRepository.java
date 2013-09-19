/**
 * 
 */
package org.mifosplatform.billing.eventmaster.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Interface for {@link EventDetails} Repository
 * extends {@link JpaRepository} and {@link JpaSpecificationExecutor}
 * 
 * @author pavani
 *
 */
public interface EventDetailsRepository extends
		JpaRepository<EventDetails, Long>,
		JpaSpecificationExecutor<EventDetails> {

}
