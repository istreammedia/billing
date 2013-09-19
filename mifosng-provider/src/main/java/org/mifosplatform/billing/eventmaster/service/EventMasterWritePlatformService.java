/**
 * 
 */
package org.mifosplatform.billing.eventmaster.service;

import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

/**
 * Interface for {@link EventMaster} Write Servcie
 * 
 * @author pavani
 *
 */
public interface EventMasterWritePlatformService {

	
	/**
	 * Method to Create {@link EventMaster}
	 * 
	 * @param command
	 * @return
	 */
	CommandProcessingResult createEventMaster (JsonCommand command); 
	
	/**
	 * Method to Update {@link EventMaster}
	 * 
	 * @param command
	 * @return
	 */
	CommandProcessingResult updateEventMaster(JsonCommand command);
	
	/**
	 * Method to Delete {@link EventMaster}
	 * 
	 * @param eventId
	 * @return
	 */
	CommandProcessingResult deleteEventMaster(Long eventId);
}
