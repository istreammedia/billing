/**
 * 
 */
package org.mifosplatform.billing.eventpricing.service;

import java.util.ArrayList;
import java.util.List;

import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.billing.eventmaster.domain.EventMasterRepository;
import org.mifosplatform.billing.eventpricing.data.EventPricingData;
import org.mifosplatform.billing.eventpricing.domain.EventPricing;
import org.mifosplatform.billing.eventpricing.domain.EventPricingRepository;
import org.mifosplatform.billing.eventpricing.serialization.EventPricingFromApiJsonDeserializer;
import org.mifosplatform.billing.pricing.exceptions.DuplicatEventPrice;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.updatecomparing.UpdateCompareUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link Service} Class for {@link EventPricing} Write Service
 * implements {@link EventPricingWritePlatformService}
 * 
 * @author pavani
 *
 */
@Service
public class EventPricingWritePlatformServiceImpl implements
		EventPricingWritePlatformService {

	private PlatformSecurityContext context;
	private EventPricingFromApiJsonDeserializer apiJsonDeserializer;
	private EventPricingRepository eventPricingRepository;
	private EventPricingReadPlatformService eventPricingReadPlatformService;
	private final EventMasterRepository eventMasterRepository;
	final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("eventPricing");
	
	
	@Autowired
	public EventPricingWritePlatformServiceImpl(PlatformSecurityContext context,
												EventPricingFromApiJsonDeserializer apiJsonDeserializer,
												EventPricingRepository eventPricingRepository,final EventMasterRepository eventMasterRepository, 
												EventPricingReadPlatformService eventPricingReadPlatformService) {
		this.context = context;
		this.apiJsonDeserializer = apiJsonDeserializer;
		this.eventPricingRepository = eventPricingRepository;
		this.eventPricingReadPlatformService = eventPricingReadPlatformService;
		this.eventMasterRepository=eventMasterRepository;
		
	}

	@Transactional
	@Override
	public CommandProcessingResult createEventPricing(JsonCommand command) {
		try {
			this.context.authenticatedUser();
			this.apiJsonDeserializer.validateForCreate(command.json());
			Long eventId = command.longValueOfParameterNamed("eventId");
			EventMaster eventMaster=this.eventMasterRepository.findOne(eventId);
			final EventPricing eventPricing = EventPricing.fromJson(command,eventMaster);
			List<EventPricingData> eventDetails  = this.eventPricingReadPlatformService.retrieventPriceData(command.entityId());
				for (EventPricingData eventDetail:eventDetails){
					if(eventPricing.getFormatType().equalsIgnoreCase(eventDetail.getFormatType()) &&
				   	   eventPricing.getClientType() == eventDetail.getClientType() &&
					   eventPricing.getOptType().equalsIgnoreCase(eventDetail.getOptType())) {
						throw new DuplicatEventPrice(eventPricing.getFormatType());
					} 
				}
				this.eventPricingRepository.save(eventPricing);
			return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(eventPricing.getId()).build();
		} catch (DataIntegrityViolationException dve) {
			return new CommandProcessingResult(Long.valueOf(-1));
		}
	}
	
	
	public CommandProcessingResult updateEventPricing(JsonCommand command) {
		try{
			this.apiJsonDeserializer.validateForCreate(command.json());
			EventPricing eventPrice = this.eventPricingRepository.findOne(command.entityId());
			EventMaster eventMaster=eventPrice.getEventId();
			EventPricing newEventPricing = EventPricing.fromJson(command,eventMaster);
			eventPrice = (EventPricing) UpdateCompareUtil.compare(eventPrice, newEventPricing);
			this.eventPricingRepository.save(eventPrice);
			return new CommandProcessingResultBuilder().withEntityId(eventPrice.getId()).withCommandId(command.commandId()).build();
		} catch(DataIntegrityViolationException dev) {
			return CommandProcessingResult.empty();
		}
	}
	
	@Transactional
	@Override
	public CommandProcessingResult deleteEventPricing(JsonCommand command) {
		try{
			EventPricing eventPricing = this.eventPricingRepository.findOne(command.entityId());
			eventPricing.setIsDeleted('y');
			this.eventPricingRepository.save(eventPricing);
			return new CommandProcessingResultBuilder().withEntityId(eventPricing.getId()).build();
		} catch(DataIntegrityViolationException dev) {
			return CommandProcessingResult.empty();
		}
	}

	private void throwExceptionIfValidationWarningsExist(List<ApiParameterError> dataValidationErrors) {
         throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Event Price Already Exists.", dataValidationErrors);
    }
}
