package org.mifosplatform.billing.processrequest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProcessRequestRepository  extends
JpaRepository<ProcessRequest, Long>,
JpaSpecificationExecutor<ProcessRequest>{

}
