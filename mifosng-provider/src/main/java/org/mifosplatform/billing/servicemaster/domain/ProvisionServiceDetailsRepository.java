package org.mifosplatform.billing.servicemaster.domain;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface ProvisionServiceDetailsRepository extends JpaRepository<ProvisionServiceDetails,Long>, 
JpaSpecificationExecutor<ProvisionServiceDetails>{
	
	ProvisionServiceDetails findOneByServiceId(Long serviceId);

}
