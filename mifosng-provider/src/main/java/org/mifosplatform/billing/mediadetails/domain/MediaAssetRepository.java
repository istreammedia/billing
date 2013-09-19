package org.mifosplatform.billing.mediadetails.domain;

import org.mifosplatform.billing.media.domain.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MediaAssetRepository  extends
JpaRepository<MediaAsset, Long>,
JpaSpecificationExecutor<MediaAsset>{

}
