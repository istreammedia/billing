package org.mifosplatform.billing.eventpricing.api;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.eventmaster.data.EventDetailsData;
import org.mifosplatform.billing.eventmaster.service.EventMasterReadPlatformService;
import org.mifosplatform.billing.eventpricing.data.ClientTypeData;
import org.mifosplatform.billing.eventpricing.data.EventPricingData;
import org.mifosplatform.billing.eventpricing.domain.EventPricing;
import org.mifosplatform.billing.eventpricing.service.EventPricingReadPlatformService;
import org.mifosplatform.billing.mediadetails.data.MediaAssetLocationDetails;
import org.mifosplatform.billing.mediadetails.service.MediaAssetDetailsReadPlatformService;
import org.mifosplatform.billing.pricing.service.PriceReadPlatformService;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiParameterHelper;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class for Creating, Updating and Deleting {@link EventPricing}
 * 
 * @author pavani
 *
 */
@Path("/eventprice")
@Component
@Scope("singleton")
public class EventPricingApiResource {
	
	private final Set<String> RESPONSE_PARAMETERS = new HashSet<String>(Arrays.asList("id","eventId","formatType","optType","clientType","discount","price",
			"eventName","clientTypeId"));
	
	private EventMasterReadPlatformService eventMasterReadPlatformService;
	private MediaAssetDetailsReadPlatformService assetDetailsReadPlatformService;
	private EventPricingReadPlatformService eventPricingReadService;
	private ApiRequestParameterHelper apiRequestParameterHelper;
	private DefaultToApiJsonSerializer<EventPricingData> apiJsonSerializer;
	private PriceReadPlatformService priceReadPlatformService;
	private PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService;
	
	@Autowired
	public EventPricingApiResource(final EventMasterReadPlatformService eventMasterReadPlatformService,
								   final MediaAssetDetailsReadPlatformService assetReadPlatformService,
								   final EventPricingReadPlatformService eventPricingReadService,
								   final ApiRequestParameterHelper apiRequestParameterHelper,
								   final DefaultToApiJsonSerializer<EventPricingData> apiJsonSerializer,
								   final PriceReadPlatformService priceReadPlatformService,
								   final PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService) {
		this.eventMasterReadPlatformService = eventMasterReadPlatformService;
		this.assetDetailsReadPlatformService = assetReadPlatformService;
		this.eventPricingReadService = eventPricingReadService;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.apiJsonSerializer = apiJsonSerializer;
		this.priceReadPlatformService = priceReadPlatformService;
		this.commandSourceWritePlatformService = commandSourceWritePlatformService;
	}

	/**
	 * Method to retrieve {@link EventPricing} Data
	 * 
	 * @param eventId
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Path("template")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String retrievePriceTemplateData(@QueryParam("eventId") Long eventId,@Context UriInfo uriInfo) {
		Set<String> responseParameters = ApiParameterHelper.extractFieldsForResponseIfProvided(uriInfo.getQueryParameters());
		responseParameters.addAll(RESPONSE_PARAMETERS);
		EventPricingData templateData = handleTemplateRelatedData(eventId);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.apiJsonSerializer.serialize(settings, templateData, RESPONSE_PARAMETERS);
	}
	public EventPricingData handleTemplateRelatedData(Long eventId) {
		List<EnumOptionData> optType = this.eventMasterReadPlatformService.retrieveOptTypeData();
		List<EventDetailsData> details = this.eventMasterReadPlatformService.retrieveEventDetailsData(eventId.intValue());
		List<ClientTypeData> clientType = this.eventPricingReadService.clientType();
		List<MediaAssetLocationDetails> format = this.assetDetailsReadPlatformService.retrieveMediaAssetLocationData(details.get(0).getMediaId());
		List<DiscountMasterData> discountdata = this.priceReadPlatformService.retrieveDiscountDetails();
 		EventPricingData pricingData = new EventPricingData(optType,format,discountdata,clientType,eventId);
		return pricingData;
	}
	
	/**
	 * Method to retrieve single {@link EventPricing} for eventId
	 * 
	 * @param eventId
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Path("{eventId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String retrievePriceData(@PathParam("eventId") Long eventId, @Context UriInfo uriInfo) {
		List<EventPricingData> priceData = this.eventPricingReadService.retrieventPriceData(eventId);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.apiJsonSerializer.serialize(settings, priceData, RESPONSE_PARAMETERS);
	}
	
	/**
	 * Method to Create single {@link EventPricing} for eventId
	 * 
	 * @param eventId
	 * @param jsonBodyRequest
	 * @return
	 */
	@POST
	@Path("{eventId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String createEventPricing(@PathParam("eventId") Long eventId, final String jsonBodyRequest) {
		final CommandWrapper commandRequest = new CommandWrapperBuilder().createEventPricing(eventId).withJson(jsonBodyRequest).build();
		final CommandProcessingResult result  = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		
		return this.apiJsonSerializer.serialize(result);
	}
	
	/**
	 * Method to get details for single {@link EventPricing} for eventPriceId
	 * 
	 * @param eventPriceId
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Path("{id}/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateEventPricing(@PathParam("id") final Long eventPriceId, @Context final UriInfo uriInfo) {
		
		Set<String> responseParameters = ApiParameterHelper.extractFieldsForResponseIfProvided(uriInfo.getQueryParameters());
		responseParameters.addAll(RESPONSE_PARAMETERS);

		EventPricingData eventPricing = this.eventPricingReadService.retrieventPriceDetails(eventPriceId);
		List<EnumOptionData> optType = this.eventMasterReadPlatformService.retrieveOptTypeData();
		List<EventDetailsData> details = this.eventMasterReadPlatformService.retrieveEventDetailsData(eventPricing.getEventId().intValue());
		List<ClientTypeData> clientType = this.eventPricingReadService.clientType();
		List<MediaAssetLocationDetails> format = this.assetDetailsReadPlatformService.retrieveMediaAssetLocationData(details.get(0).getMediaId());
		List<DiscountMasterData> discountdata = this.priceReadPlatformService.retrieveDiscountDetails();
		eventPricing.setClientTypes(clientType);
		eventPricing.setOptTypes(optType);
		eventPricing.setFormat(format);
		eventPricing.setDiscountdata(discountdata);
		
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		
		return this.apiJsonSerializer.serialize(settings,eventPricing,RESPONSE_PARAMETERS);
	}
	
	/**
	 * Method to update single {@link EventPricing} for eventPriceId
	 * 
	 * @param eventPriceId
	 * @param jsonRequestBody
	 * @return
	 */
	@PUT
	@Path("{eventPriceId}/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateEventPrice(@PathParam("eventPriceId")Long eventPriceId, final String jsonRequestBody) {
		final CommandWrapper commandRequest = new CommandWrapperBuilder().updateEventPricing(eventPriceId).withJson(jsonRequestBody).build();
		final CommandProcessingResult result = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		
		return this.apiJsonSerializer.serialize(result);
	}
	
	/**
	 * Method to delete single {@link EventPricing} for eventPriceId
	 * 
	 * @param eventPriceId
	 * @return
	 */
	@DELETE
	@Path("{eventPriceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteEventPricing(@PathParam("eventPriceId")Long eventPriceId) {
		final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteEventPricing(eventPriceId).build();
		final CommandProcessingResult result = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		
		return this.apiJsonSerializer.serialize(result);
	}
}
