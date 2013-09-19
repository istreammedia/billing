package org.mifosplatform.billing.epgprogramguide.service;

import java.util.List;

import org.mifosplatform.billing.epgprogramguide.data.EpgProgramGuideData;

public interface EpgProgramGuideReadPlatformService {
	


	public List<EpgProgramGuideData> retrivePrograms(String channelName, Long counter);
}
