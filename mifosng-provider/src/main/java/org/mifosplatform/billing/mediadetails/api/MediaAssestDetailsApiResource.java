package org.mifosplatform.billing.mediadetails.api;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.eventpricing.data.EventPricingData;
import org.mifosplatform.billing.mediadetails.data.MediaAssetDetailsData;
import org.mifosplatform.billing.mediadetails.data.MediaLocationData;
import org.mifosplatform.billing.mediadetails.exception.NoMediaDeviceFoundException;
import org.mifosplatform.billing.mediadetails.service.MediaAssetDetailsReadPlatformService;
import org.mifosplatform.billing.mediadevice.data.MediaDeviceData;
import org.mifosplatform.billing.mediadevice.service.MediaDeviceReadPlatformService;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Path("/assetdetails")
@Component
@Scope("singleton")
public class MediaAssestDetailsApiResource {
	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("mediaId","mediaTitle","type","classType","overview","subject",
    		"mediaImage","duration","contentProvider","rated","mediaRating","ratingCount","location","status","releaseDate","genres","languages","filmLocations",
    		"writers","directors","actors","countries","filmLocationsdata","mediaassetAttributes"));
    private final String resourceNameForPermissions = "ASSESTS";
	private final PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<MediaAssetDetailsData> toApiJsonSerializer;
	private final MediaDeviceReadPlatformService deviceReadPlatformService;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private final MediaAssetDetailsReadPlatformService assetDetailsReadPlatformService;
	private final FromJsonHelper fromJsonHelper;
	
	    @Autowired
	    public MediaAssestDetailsApiResource(final PlatformSecurityContext context, final DefaultToApiJsonSerializer<MediaAssetDetailsData> toApiJsonSerializer,
	    final ApiRequestParameterHelper apiRequestParameterHelper,final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
	    final MediaAssetDetailsReadPlatformService assetDetailsReadPlatformService,final FromJsonHelper fromJsonHelper,final MediaDeviceReadPlatformService deviceReadPlatformService) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.assetDetailsReadPlatformService=assetDetailsReadPlatformService;
		        this.fromJsonHelper=fromJsonHelper;
		       
		        this.deviceReadPlatformService=deviceReadPlatformService;
		        
		    }	
	@GET
	@Path("{category}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveMediaAssestdata(@PathParam("category") final Long category,@QueryParam("deviceId") final String deviceId,
			@QueryParam("eventId")  final Long eventId,@Context final UriInfo uriInfo) {
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		MediaDeviceData details=this.deviceReadPlatformService.retrieveDeviceDetails(deviceId);
		
		 if(details == null){
			 throw new NoMediaDeviceFoundException();
		 }
		 MediaAssetDetailsData assetData = this.assetDetailsReadPlatformService.retrieveMediaAssetDetailsData(category);
			final List<String> genres=this.assetDetailsReadPlatformService.retrieveGenresData(assetData.getMediaId());
			final List<String> production=this.assetDetailsReadPlatformService.retrieveProductions(assetData.getMediaId());
			final List<MediaLocationData> filmingLocation=this.assetDetailsReadPlatformService.retrieveFilmLocation(assetData.getMediaId());
			final List<String>  writers=this.assetDetailsReadPlatformService.retrieveWriters(assetData.getMediaId());
			final List<String> directors=this.assetDetailsReadPlatformService.retrieveDirectors(assetData.getMediaId());
			final List<String> actors=this.assetDetailsReadPlatformService.retrieveActors(assetData.getMediaId());
			final List<String> country=this.assetDetailsReadPlatformService.retrieveCountry(assetData.getMediaId());
			List<EventPricingData> eventPricingData=this.assetDetailsReadPlatformService.getEventPriceDetails(eventId,details.getClientType());
			if(eventPricingData.size() ==0)
			{
				 eventPricingData=this.assetDetailsReadPlatformService.getEventPriceDetails(eventId,"ALL");
			}
			assetData=new MediaAssetDetailsData(assetData.getMediaId(),assetData.getTitle(),assetData.getType(),assetData.getClassType(),
					assetData.getOverview(),assetData.getSubject(),assetData.getImage(),assetData.getDuration(),assetData.getContentProvider(),assetData.getRated(),
					assetData.getRating(),assetData.getRatingCount(),assetData.getLocation(),assetData.getStatus(),assetData.getReleaseDate(),genres,production,
					filmingLocation,writers,directors,actors,country,eventPricingData);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, assetData, RESPONSE_DATA_PARAMETERS);
	}

}
