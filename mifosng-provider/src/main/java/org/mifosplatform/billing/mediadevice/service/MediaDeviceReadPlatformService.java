package org.mifosplatform.billing.mediadevice.service;

import java.util.List;

import org.mifosplatform.billing.mediadevice.data.MediaDeviceData;
import org.mifosplatform.billing.plan.data.PlanData;


public interface MediaDeviceReadPlatformService {

	List<MediaDeviceData> retrieveDeviceDataDetails(String deviceId);
	MediaDeviceData retrieveDeviceDetails(String deviceId);
	List<PlanData> retrievePlanDetails(Long clientId);
	List<PlanData> retrievePlanPostpaidDetails(Long clientId);

}
