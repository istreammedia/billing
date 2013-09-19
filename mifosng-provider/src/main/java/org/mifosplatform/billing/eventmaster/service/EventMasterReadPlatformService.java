package org.mifosplatform.billing.eventmaster.service;

import java.util.List;

import org.mifosplatform.billing.eventmaster.data.EventDetailsData;
import org.mifosplatform.billing.eventmaster.data.EventMasterData;
import org.mifosplatform.billing.eventmaster.domain.EventDetails;
import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.billing.eventmaster.domain.OptType;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

import ch.qos.logback.core.status.Status;

/**
 * Interface for {@link EventMaster} Read Service
 * 
 * @author pavani
 *
 */
public interface EventMasterReadPlatformService {


	/**
	 * Method for retrieving {@link OptType} {@link EnumOptionData}
	 * 
	 * @return
	 */
	List<EnumOptionData> retrieveOptTypeData();

	/**
	 * Method for retrieving {@link Status} {@link EnumOptionData}
	 * 
	 * @return
	 */
	List<EnumOptionData> retrieveNewStatus();
	
	/**
	 * Method for retrieving {@link EventMaster} {@link List} 
	 * 
	 * @return
	 */
	List<EventMasterData> retrieveEventMasterData();
	
	/**
	 * Method for retrieving single {@link EventMaster}
	 * 
	 * @param eventId
	 * @return
	 */
	EventMasterData retrieveEventMasterDetails(Integer eventId);
	
	/**
	 * Method for retrieving single {@link EventDetails}
	 * 
	 * @param eventId
	 * @return
	 */
	EventDetailsData retrieveEventDetails(Integer eventId);
	
	/**
	 * Method for retrieving {@link EventDetails} {@link List}
	 * 
	 * @param eventId
	 * @return
	 */
	List<EventDetailsData> retrieveEventDetailsData(Integer eventId);
	
}
