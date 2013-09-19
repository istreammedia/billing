package org.mifosplatform.billing.media.api;
import java.util.ArrayList;
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

import org.mifosplatform.billing.media.data.MediaAssetData;
import org.mifosplatform.billing.media.data.MediaassetAttribute;
import org.mifosplatform.billing.media.data.MediaassetAttributeData;
import org.mifosplatform.billing.media.service.MediaAssetReadPlatformService;
import org.mifosplatform.billing.mediadetails.data.MediaLocationData;
import org.mifosplatform.billing.mediadevice.data.MediaDeviceData;
import org.mifosplatform.billing.mediadevice.service.MediaDeviceReadPlatformService;
import org.mifosplatform.billing.paymode.data.McodeData;
import org.mifosplatform.billing.plan.service.PlanReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.data.MediaEnumoptionData;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Path("/assets")
@Component
@Scope("singleton")
public class MediaAssestApiResource {
	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("mediaId","mediaTitle","type","classType","overview","subject",
    		"mediaImage","duration","contentProvider","rated","mediaRating","ratingCount","location","status","releaseDate","genres","languages","filmLocations",
    		"writers","directors","actors","countries","noOfPages","mediaStatus","mediaAttributes","mediaFormat","mediaTypeData","mediaCategeorydata","mediaLanguageData"));
    private final String resourceNameForPermissions = "MEDIAASSET";
	private final PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<MediaAssetData> toApiJsonSerializer;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private final MediaAssetReadPlatformService mediaAssetReadPlatformService;
	private final MediaDeviceReadPlatformService deviceReadPlatformService;
	private final FromJsonHelper fromJsonHelper;
	private final FromJsonHelper fromApiJsonHelper;
	private final PlanReadPlatformService planReadPlatformService;
	 @Autowired
	    public MediaAssestApiResource(final PlatformSecurityContext context, final DefaultToApiJsonSerializer<MediaAssetData> toApiJsonSerializer,
	    final ApiRequestParameterHelper apiRequestParameterHelper,final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
	    final MediaAssetReadPlatformService mediaAssetReadPlatformService,final FromJsonHelper fromJsonHelper,final FromJsonHelper fromApiJsonHelper,
	    final MediaDeviceReadPlatformService deviceReadPlatformService,final PlanReadPlatformService planReadPlatformService) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.mediaAssetReadPlatformService=mediaAssetReadPlatformService;
		        this.deviceReadPlatformService=deviceReadPlatformService;
		        this.fromJsonHelper=fromJsonHelper;
		        this.fromApiJsonHelper=fromApiJsonHelper;
		        this.planReadPlatformService=planReadPlatformService;
		    }	
	 
	 
	//Get All Media Asset Details
	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveMediaAssestdata(@QueryParam("deviceId") final String deviceId, @QueryParam("pageNo")  Long pageNum,
			@QueryParam("filterType") final String filterType,@Context final UriInfo uriInfo) {
          context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
          MediaDeviceData details=this.deviceReadPlatformService.retrieveDeviceDetails(deviceId);
            Long pageNo=new Long(0);
            if(pageNum == null || pageNum == 0){
        	  pageNum=new Long(0);
           }else{
              pageNo=(pageNum*10);
          }
            List<MediaAssetData> data=new ArrayList<MediaAssetData>();
           if(filterType.equalsIgnoreCase("ALL")){
        	   
        	   data = this.mediaAssetReadPlatformService.retrievemediaAssetdata(pageNo);
        	   
        	   final String queryFOrPages=" SELECT count(0)  FROM b_media_asset m inner join b_event_detail ed on ed.media_id = m.id"
			                   +" inner join b_event_master em on em.id = ed.event_id  GROUP BY m.id  having  count( ed.media_id) = 1 ";
         	  Long noOfPages=this.mediaAssetReadPlatformService.retrieveNoofPages(queryFOrPages);
         	  for(MediaAssetData assetData:data){
         		  
         		  List<MediaLocationData> locationData=this.mediaAssetReadPlatformService.retrievemediaAssetLocationdata(assetData.getMediaId());
         	  }
         	  
         	  data.add(new MediaAssetData(noOfPages,pageNum));
        	  
          }
          
          else if(filterType.equalsIgnoreCase("RELEASE")){
        	  
		     data = this.mediaAssetReadPlatformService.retrievemediaAssetdatabyNewRealease(pageNo);
		     final String query=" SELECT count(0) FROM b_media_asset m INNER JOIN b_event_detail ed ON ed.media_id = m.id"
		    		 +" INNER JOIN b_event_master em  ON em.id = ed.event_id where m.release_date <= adddate(now(),INTERVAL -3 MONTH)"
		    		 +" group by m.id  having count(distinct ed.event_id) >=1 ";
	         Long noOfPages=this.mediaAssetReadPlatformService.retrieveNoofPages(query);
	         data.add(new MediaAssetData(noOfPages,pageNum));
		     
          }
          
          else if(filterType.equalsIgnoreCase("RATING")){
        	  
        	  data=this.mediaAssetReadPlatformService.retrievemediaAssetdatabyRating(pageNo);
        	  final String query=" SELECT count(0) FROM b_media_asset m INNER JOIN b_event_detail ed ON ed.media_id = m.id"
        			  +" INNER JOIN b_event_master em ON em.id = ed.event_id group by m.id  having count(distinct ed.event_id) >=1 ";
	          Long noOfPages=this.mediaAssetReadPlatformService.retrieveNoofPages(query);
	          data.add(new MediaAssetData(noOfPages,pageNum));
          }
          
          else if(filterType.equalsIgnoreCase("DISCOUNT")){
        	  
        	  data=this.mediaAssetReadPlatformService.retrievemediaAssetdatabyDiscountedMovies(pageNo);
        	  final String query=" SELECT count(0) FROM b_media_asset m INNER JOIN b_event_detail ed ON ed.media_id = m.id"
        			  +" INNER JOIN b_event_master em  ON em.id = ed.event_id inner join  b_event_pricing ep on em.id=ep.event_id"
        			  +" where discount_id>=1  group by m.id  having count(distinct ed.event_id) >=1";
	          Long noOfPages=this.mediaAssetReadPlatformService.retrieveNoofPages(query);
	          data.add(new MediaAssetData(noOfPages,pageNum));
          }
          
          else if(filterType.equalsIgnoreCase("PROMOTION")){
        	  
        	  data=this.mediaAssetReadPlatformService.retrievemediaAssetdatabyPromotionalMovies(pageNo);
        	  final String query=" SELECT count(0)  FROM b_media_asset m inner join b_event_detail ed on ed.media_id = m.id"
	                   +" inner join b_event_master em on em.id = ed.event_id  group by m.id  having count(distinct ed.event_id) >1 ";
	          Long noOfPages=this.mediaAssetReadPlatformService.retrieveNoofPages(query);
	          data.add(new MediaAssetData(noOfPages,pageNum));
          }
          
          else if(filterType.equalsIgnoreCase("COMING")){
        	  
        	  data=this.mediaAssetReadPlatformService.retrievemediaAssetdatabyComingSoonMovies(pageNo);
        	  final String query=" SELECT count(0) FROM b_media_asset m where category_id=19 ";
	          Long noOfPages=this.mediaAssetReadPlatformService.retrieveNoofPages(query);
	          data.add(new MediaAssetData(noOfPages,pageNum));
          }
          
          else if(filterType.equalsIgnoreCase("WATCHED")){
        	  
        	  data=this.mediaAssetReadPlatformService.retrievemediaAssetdatabyMostWatchedMovies(pageNo);
        	  final String query="SELECT count(0) FROM b_media_asset m inner join b_event_detail ed on m.id=ed.media_id  inner " +
        	  		" JOIN b_eventorder eo  ON (eo.event_id = ed.event_id)";
        	  Long noOfPages=this.mediaAssetReadPlatformService.retrieveNoofPages(query);
        	  data.add(new MediaAssetData(noOfPages,pageNum));
          }
                    
          else {
        	  
        	  data=this.mediaAssetReadPlatformService.retrievemediaAssetdatabySearching(pageNo,filterType);
        	  final String query="SELECT count(0) FROM b_media_asset m inner join b_event_detail ed on m.id=ed.media_id  inner " +
        	  		" JOIN b_eventorder eo  ON (eo.event_id = ed.event_id)";
        	  Long noOfPages=this.mediaAssetReadPlatformService.retrieveNoofPages(query);
        	  data.add(new MediaAssetData(noOfPages,pageNum));
          }
   	  
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
	}
	
		
   @POST
   @Consumes({ MediaType.APPLICATION_JSON })
   @Produces({ MediaType.APPLICATION_JSON })
   public String InsertMediaAssetData(final String apiRequestBodyAsJson) {

   final CommandWrapper commandRequest=new CommandWrapperBuilder().createMediaAsset().withJson(apiRequestBodyAsJson).build();
   final CommandProcessingResult result=this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
   return this.toApiJsonSerializer.serialize(result);
}
   
    @GET
    @Path("mediadata")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveAllMediaAssestdata(@Context final UriInfo uriInfo) {
         context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
         List<MediaAssetData> data   = this.mediaAssetReadPlatformService.retrieveAllAssetdata();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
		
		

}
    
    
    @GET
    @Path("template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveMediaAssestTemplatedata(@Context final UriInfo uriInfo) {
         context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
         
         MediaAssetData assetData=handleTEmplateData();
    	
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, assetData, RESPONSE_DATA_PARAMETERS);
    
}
    
    private MediaAssetData handleTEmplateData() {
    	 List<EnumOptionData> status = this.planReadPlatformService.retrieveNewStatus();
         List<MediaassetAttribute> data   = this.mediaAssetReadPlatformService.retrieveMediaAttributes();
         List<MediaassetAttribute> mediaFormat=this.mediaAssetReadPlatformService.retrieveMediaFormatType();
         List<MediaEnumoptionData> mediaTypeData =this.mediaAssetReadPlatformService.retrieveMediaTypeData();
         List<McodeData> mediaCategeorydata=this.mediaAssetReadPlatformService.retrieveMedaiCategory();
         List<McodeData> languageCategeory=this.mediaAssetReadPlatformService.retrieveLanguageCategeories();
         return new MediaAssetData(null,null,null,status,data,mediaFormat,mediaTypeData,mediaCategeorydata,languageCategeory);
	}


	/*@GET
    @Path("{mediaId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveSingleMediaAssestDetails(@PathParam("mediaId") final Long mediaId,@Context final UriInfo uriInfo) {
         context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
        
         MediaAssetData mediaAssetData=this.mediaAssetReadPlatformService.retrievemediaAsset(mediaId);
         List<EnumOptionData> status = this.planReadPlatformService.retrieveNewStatus();
         List<MediaassetAttribute> data   = this.mediaAssetReadPlatformService.retrieveMediaAttributes();
         List<MediaassetAttribute> mediaFormat=this.mediaAssetReadPlatformService.retrieveMediaFormatType();
         List<MediaEnumoptionData> mediaTypeData =this.mediaAssetReadPlatformService.retrieveMediaTypeData();
         List<McodeData> mediaCategeorydata=this.mediaAssetReadPlatformService.retrieveMedaiCategory();
         List<McodeData> languageCategeory=this.mediaAssetReadPlatformService.retrieveLanguageCategeories();
         MediaAssetData assetData=new MediaAssetData(status,data,mediaFormat,mediaTypeData,mediaCategeorydata,languageCategeory);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, assetData, RESPONSE_DATA_PARAMETERS);
		
}*/
	@GET
    @Path("{mediaId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveSingleMediaAssestDetails(@PathParam("mediaId") final Long mediaId,@Context final UriInfo uriInfo) {
         context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
       
         MediaAssetData mediaAssetData=this.mediaAssetReadPlatformService.retrievemediaAsset(mediaId);
         List<MediaassetAttributeData> mediaassetAttributes=this.mediaAssetReadPlatformService.retrieveMediaassetAttributesData(mediaId);
         List<MediaLocationData> mediaLocationData=this.mediaAssetReadPlatformService.retrievemediaAssetLocationdata(mediaId);
         List<EnumOptionData> status = this.planReadPlatformService.retrieveNewStatus();
         List<MediaassetAttribute> data   = this.mediaAssetReadPlatformService.retrieveMediaAttributes();
         List<MediaassetAttribute> mediaFormat=this.mediaAssetReadPlatformService.retrieveMediaFormatType();
         List<MediaEnumoptionData> mediaTypeData =this.mediaAssetReadPlatformService.retrieveMediaTypeData();
         List<McodeData> mediaCategeorydata=this.mediaAssetReadPlatformService.retrieveMedaiCategory();
         List<McodeData> mediaLanguageData=this.mediaAssetReadPlatformService.retrieveLanguageCategeories();
         MediaAssetData assetData=new MediaAssetData(mediaAssetData,mediaassetAttributes,mediaLocationData,status,data,mediaFormat,mediaTypeData,mediaCategeorydata,mediaLanguageData);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, assetData, RESPONSE_DATA_PARAMETERS);
      }
	
	@PUT
	@Path("{assetId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	
	public String assetEditData(@PathParam("assetId") final Long assetId,final String apiRequestBodyAsJson) {
		
		 final CommandWrapper commandRequest=new CommandWrapperBuilder().updateAsset(assetId).withJson(apiRequestBodyAsJson).build();
		   final CommandProcessingResult result=this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		   return this.toApiJsonSerializer.serialize(result);
	}
	

	@DELETE
	@Path("{assetId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String deleteAssetData(@PathParam("assetId") final Long assetId,final String apiRequestBodyAsJson) {
		
		 final CommandWrapper commandRequest=new CommandWrapperBuilder().deleteAsset(assetId).withJson(apiRequestBodyAsJson).build();
		   final CommandProcessingResult result=this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		   return this.toApiJsonSerializer.serialize(result);
	}
	
    }