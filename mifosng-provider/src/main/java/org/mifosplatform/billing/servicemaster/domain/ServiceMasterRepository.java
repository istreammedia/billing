package org.mifosplatform.billing.servicemaster.domain;
import org.mifosplatform.billing.servicemaster.domain.ServiceMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface ServiceMasterRepository extends JpaRepository<ServiceMaster, Long>, JpaSpecificationExecutor<ServiceMaster>{

}
