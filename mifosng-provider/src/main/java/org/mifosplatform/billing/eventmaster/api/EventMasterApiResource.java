/**
 * 
 */
package org.mifosplatform.billing.eventmaster.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.eventmaster.data.EventDetailsData;
import org.mifosplatform.billing.eventmaster.data.EventMasterData;
import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.billing.eventmaster.service.EventMasterReadPlatformService;
import org.mifosplatform.billing.item.data.ChargesData;
import org.mifosplatform.billing.item.service.ItemReadPlatformService;
import org.mifosplatform.billing.media.data.MediaAssetData;
import org.mifosplatform.billing.media.service.MediaAssetReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiParameterHelper;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class to Create Update and Delete {@link EventMaster}
 * 
 * @author pavani
 *
 */
@Path("/eventmaster")
@Component
@Scope("singleton")
public class EventMasterApiResource {
	
	private final Set<String> RESPONSE_PARAMETERS = new HashSet<String>(Arrays.asList("id","eventName","eventDescription","status","eventStartDate","eventEndDate",
			"chargeData","eventValidity"));
	
	private PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService;
	private DefaultToApiJsonSerializer<EventMasterData> toApiJsonSerializer;
	private ApiRequestParameterHelper apiRequestParameterHelper;
	private PlatformSecurityContext context;
	private EventMasterReadPlatformService eventMasterReadPlatformService;
	private MediaAssetReadPlatformService assetReadPlatformService;
	private final ItemReadPlatformService itemReadPlatformService;
	/**
	 * @param commandSourceWritePlatformService
	 * @param toApiJsonSerializer
	 * @param context
	 * @param apiRequestParameterHelper
	 * @param assetReadPlatformService
	 * @param eventMasterReadPlatformService
	 */
	@Autowired
	public EventMasterApiResource(final PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService,
								  final DefaultToApiJsonSerializer<EventMasterData> toApiJsonSerializer,
								  final PlatformSecurityContext context,
								  final ApiRequestParameterHelper apiRequestParameterHelper,
								  final MediaAssetReadPlatformService assetReadPlatformService,
								  final EventMasterReadPlatformService eventMasterReadPlatformService,
								  final ItemReadPlatformService itemReadPlatformService) {
		this.commandSourceWritePlatformService = commandSourceWritePlatformService;
		this.toApiJsonSerializer = toApiJsonSerializer;
		this.apiRequestParameterHelper =  apiRequestParameterHelper;
		this.context = context;
		this.assetReadPlatformService = assetReadPlatformService;
		this.eventMasterReadPlatformService = eventMasterReadPlatformService;
		this.itemReadPlatformService=itemReadPlatformService;
	}
	
	/**
	 * Generic Method to get Template Related Data
	 * for {@link EventMaster}
	 * 
	 * for popUp data
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Path("template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retrieveEventMasterTempleteData(@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission("CLIENT");
		Set<String> responseParameters = ApiParameterHelper.extractFieldsForResponseIfProvided(uriInfo.getQueryParameters());
		responseParameters.addAll(RESPONSE_PARAMETERS);
		EventMasterData templetData = handleTemplateRelatedData(responseParameters);		
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	
		return this.toApiJsonSerializer.serialize(settings, templetData, RESPONSE_PARAMETERS);
	}
	public EventMasterData handleTemplateRelatedData(final Set<String> responseParameters) {
		
	//	List<MediaAssetData> mediaData = this.assetReadPlatformService.retrieveAllmediaAssetdata();
	    List<MediaAssetData> mediaData   = this.assetReadPlatformService.retrieveAllAssetdata();
		List<EnumOptionData> statusData = this.eventMasterReadPlatformService.retrieveNewStatus();
		List<EnumOptionData> optType = this.eventMasterReadPlatformService.retrieveOptTypeData();
		List<ChargesData> chargeDatas = this.itemReadPlatformService.retrieveChargeCode();
		EventMasterData singleEvent  = new EventMasterData(mediaData,statusData,optType,chargeDatas);
		
		return singleEvent;	
	}
	
	
	/**
	 * Generic Method for Retrieving single {@link EventMaster}
	 * 
	 * @param eventId
	 * @param uriInfo
	 * @return
	 */
	@SuppressWarnings("unused")
	@GET
	@Path("{eventId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retrieveEventMaster(@PathParam("eventId")Integer eventId,@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission("CLIENT");
		Set<String> responseParameters = ApiParameterHelper.extractFieldsForResponseIfProvided(uriInfo.getQueryParameters());
		responseParameters.addAll(RESPONSE_PARAMETERS);
		List<MediaAssetData> mediaData   = this.assetReadPlatformService.retrieveAllAssetdata();
		List<EnumOptionData> statusData = this.eventMasterReadPlatformService.retrieveNewStatus();
		List<EnumOptionData> optType = this.eventMasterReadPlatformService.retrieveOptTypeData();
		List<EventDetailsData> details = this.eventMasterReadPlatformService.retrieveEventDetailsData(eventId);
		List<ChargesData> chargeDatas = this.itemReadPlatformService.retrieveChargeCode();
		EventMasterData event = this.eventMasterReadPlatformService.retrieveEventMasterDetails(eventId);
	
		int size = mediaData.size();
		int selectedSize = details.size();
		for(int i=0;i<selectedSize;i++) {
			Long selected = details.get(i).getMediaId();
			for(int j=0;j<size;j++) {
				Long available = mediaData.get(j).getMediaId();
				if(selected == available) {
					mediaData.remove(j);
					size--;
				}
			}
		}
		
		event.setMediaAsset(mediaData);
		event.setOptType(optType);
		event.setStatusData(statusData);
		event.setSelectedMedia(details);
		event.setChargeData(chargeDatas);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	
		return this.toApiJsonSerializer.serialize(settings, event, RESPONSE_PARAMETERS);
	}
	
	/**
	 * Method to retrieve {@link EventMaster}
	 * 
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retrieveEventMasterData(@Context UriInfo uriInfo) {
		
		final List<EventMasterData> data = this.eventMasterReadPlatformService.retrieveEventMasterData();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_PARAMETERS);
	}
	
	
	/**
	 * Generic Method for Posting and creating new {@link EventMaster}
	 * 
	 * @param clientId
	 * @param jsonBodyRequest
	 * @return
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String createEventMaster( final String jsonRequestBody) {
		final CommandWrapper commandRequest = new CommandWrapperBuilder().createEvent().withJson(jsonRequestBody).build();
		final CommandProcessingResult result  = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		
		return this.toApiJsonSerializer.serialize(result);
	}
	
	/**
	 * Generic Method to Update single {@link EventMaster}
	 * 
	 * @param eventId
	 * @param jsonRequestBody
	 * @return
	 */
	@PUT
	@Path("{eventId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String updateEventMaster(@PathParam("eventId")Long eventId,final String jsonRequestBody) {
		
		final CommandWrapper commandRequest = new CommandWrapperBuilder().updateEvent(eventId).withJson(jsonRequestBody).build();
		final CommandProcessingResult result  = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		
		return this.toApiJsonSerializer.serialize(result);
	}
	
	/**
	 * Generic Method to delete single {@link EventMaster}
	 * 
	 * @param eventId
	 * @return
	 */
	@DELETE
	@Path("{eventId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String deleteEventMaster(@PathParam("eventId") Long eventId) {
		final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteEvent(eventId).build();
		final CommandProcessingResult result = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		return this.toApiJsonSerializer.serialize(result);
	}
}