/**
 * 
 */
package org.mifosplatform.billing.eventmaster.service;

import java.util.List;

import org.mifosplatform.billing.eventmaster.domain.EventDetails;
import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.billing.eventmaster.domain.EventMasterRepository;
import org.mifosplatform.billing.eventmaster.serialization.EventMasterFromApiJsonDeserializer;
import org.mifosplatform.billing.media.data.MediaAssetData;
import org.mifosplatform.billing.media.domain.MediaAsset;
import org.mifosplatform.billing.media.service.MediaAssetReadPlatformService;
import org.mifosplatform.billing.mediadetails.domain.MediaAssetRepository;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.updatecomparing.UpdateCompareUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;

/**
 * {@link Service} Class for {@link EventMaster} Write Service
 * implements {@link EventMasterWritePlatformService}
 * 
 * @author pavani
 *
 */
@Service
public class EventMasterWritePlatformServiceImpl implements
		EventMasterWritePlatformService {
	
	private PlatformSecurityContext context;
	private EventMasterRepository eventMasterRepository;
	private MediaAssetRepository assetRepository;
	private EventMasterFromApiJsonDeserializer apiJsonDeserializer;
	private MediaAssetReadPlatformService assetReadPlatformService;
	
	@Autowired
	public EventMasterWritePlatformServiceImpl (final PlatformSecurityContext context,
											    final EventMasterRepository eventMasterRepository,
											    final EventMasterFromApiJsonDeserializer apiJsonDeserializer,
											    final MediaAssetRepository assetRepository,
											    final MediaAssetReadPlatformService assetReadPlatformService) {
		this.context = context;
		this.eventMasterRepository = eventMasterRepository;
		this.apiJsonDeserializer = apiJsonDeserializer;
		this.assetRepository = assetRepository;
		this.assetReadPlatformService = assetReadPlatformService;
	}

	@Transactional
	@Override
	public CommandProcessingResult createEventMaster(JsonCommand command) {
		try {
			this.context.authenticatedUser();
			Long createdbyId = context.authenticatedUser().getId();
			this.apiJsonDeserializer.validateForCreate(command.json());
			EventMaster eventMaster = EventMaster.fromJsom(command);		
			final JsonArray array = command.arrayOfParameterNamed("mediaData").getAsJsonArray();
			String[] media  = null;
			media = new String[array.size()];
			for(int i=0 ; i<array.size() ; i++) {
				media[i] = array.get(i).getAsString();
			}
			for(String mediaId : media) {
				final Long id = Long.valueOf(mediaId);
				MediaAsset mediaAsset = this.assetRepository.findOne(id);
				EventDetails detail = new EventDetails(mediaAsset.getId());
				eventMaster.addMediaDetails(detail);
			}
			eventMaster.setCreatedbyId(createdbyId);
			this.eventMasterRepository.save(eventMaster);
			
			return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(eventMaster.getId()).build();
		} catch(DataIntegrityViolationException dve) {
			return new CommandProcessingResult(Long.valueOf(-1));
		}
	}
	
	@SuppressWarnings("unused")
	@Transactional
	@Override
	public CommandProcessingResult updateEventMaster(JsonCommand command) {
		try {
			this.context.authenticatedUser();
			this.apiJsonDeserializer.validateForCreate(command.json());
			EventMaster newEvent = EventMaster.fromJsom(command);
			EventMaster oldEvent = this.eventMasterRepository.findOne(command.entityId());
			List<MediaAssetData> mediaData = this.assetReadPlatformService.retrieveAllmediaAssetdata();
			
			for(MediaAssetData data : mediaData) {
				oldEvent.getEventDetails().clear();
				final JsonArray array = command.arrayOfParameterNamed("mediaData").getAsJsonArray();
				String[] media = null;
				media  =new String[array.size()];
				
				for(int i=0;i<array.size();i++) {
					media[i] = array.get(i).getAsString();
				}
				
				for(String mediaId : media) {
					final Long id = Long.valueOf(mediaId);
					MediaAsset mediaAsset = this.assetRepository.findOne(id);
					EventDetails detail = new EventDetails(mediaAsset.getId());
					oldEvent.addMediaDetails(detail);
				}
			}
	
			oldEvent = (EventMaster) UpdateCompareUtil.compare(oldEvent,newEvent);
			this.eventMasterRepository.save(oldEvent);
			return new CommandProcessingResultBuilder().withEntityId(command.entityId()).withCommandId(command.commandId()).build();
		} catch (DataIntegrityViolationException dve) {
			return new CommandProcessingResult(Long.valueOf(-1));
		}
	}
	
	public CommandProcessingResult deleteEventMaster(Long eventId) {
		List<MediaAssetData> mediaAsset = this.assetReadPlatformService.retrieveAllmediaAssetdata();
		EventMaster event = this.eventMasterRepository.findOne(eventId);
		for(MediaAssetData data : mediaAsset) {
			EventDetails details = new EventDetails(data.getMediaId());
			details.delete(event);
		}
		event.delete();
		this.eventMasterRepository.save(event);
		return new CommandProcessingResultBuilder().withEntityId(eventId).build();
	}
	
}