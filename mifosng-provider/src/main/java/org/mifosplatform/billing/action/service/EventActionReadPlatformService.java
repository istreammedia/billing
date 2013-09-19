package org.mifosplatform.billing.action.service;

import org.mifosplatform.billing.action.data.VolumeDetailsData;

public interface EventActionReadPlatformService {

	VolumeDetailsData retrieveVolumeDetails(Long id);

}
