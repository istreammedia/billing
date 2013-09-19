package org.mifosplatform.billing.uploadstatus.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.uploadstatus.data.UploadStatusData;

public interface UploadStatusReadPlatformService {

    Collection<UploadStatusData> retrieveAllCodes();
    List<UploadStatusData> retrieveAllUploadStatusData();

    UploadStatusData retrieveCode(Long codeId);
}
