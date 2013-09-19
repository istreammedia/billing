package org.mifosplatform.billing.uploadstatus.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UploadStatusRepository extends JpaRepository<UploadStatus, Long>, JpaSpecificationExecutor<UploadStatus>{

}


