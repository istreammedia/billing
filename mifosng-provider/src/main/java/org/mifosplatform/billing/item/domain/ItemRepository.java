/**
 * 
 */
package org.mifosplatform.billing.item.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author hugo
 *
 */
public interface ItemRepository extends JpaRepository<ItemMaster,Long >,JpaSpecificationExecutor<ItemMaster>{

}
