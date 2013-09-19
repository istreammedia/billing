package org.mifosplatform.billing.randomgenerator.service;

import java.util.List;

import org.mifosplatform.billing.randomgenerator.data.RandomGeneratorData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public interface RandomGeneratorReadPlatformService {

	String retrieveIndividualPin(String pinId);
	
	List<RandomGeneratorData> getAllData();

	List<EnumOptionData> pinCategory();

	List<EnumOptionData> pinType();

	Long retrieveMaxNo(Long minNo, Long maxNo);

}
