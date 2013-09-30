package org.mifosplatform.billing.region.api;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.address.data.CountryDetails;
import org.mifosplatform.billing.address.data.StateDetails;
import org.mifosplatform.billing.address.service.AddressReadPlatformService;
import org.mifosplatform.billing.region.data.RegionData;
import org.mifosplatform.billing.region.data.RegionDetailsData;
import org.mifosplatform.billing.region.service.RegionReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.api.JsonQuery;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;



@Path("/regions")
@Component
@Scope("singleton")
public class RegionApiResource {
	
	
	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("regionCode","regionName","states","country","stateData",
			"isDefault","countryId"));
    private final String resourceNameForPermissions = "REGION";
	  private final PlatformSecurityContext context;
	    private final DefaultToApiJsonSerializer<RegionData> toApiJsonSerializer;
	    private final ApiRequestParameterHelper apiRequestParameterHelper;
	    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	    private final RegionReadPlatformService regionReadPlatformService; 
	    private final AddressReadPlatformService addressReadPlatformService;
	    private final FromJsonHelper fromJsonHelper;
	   
	    
	    @Autowired
	    public RegionApiResource(final PlatformSecurityContext context,final DefaultToApiJsonSerializer<RegionData> toApiJsonSerializer, 
	    		final ApiRequestParameterHelper apiRequestParameterHelper,final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
	    		final RegionReadPlatformService regionReadPlatformService,final AddressReadPlatformService addressReadPlatformService,final FromJsonHelper fromJsonHelper) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.regionReadPlatformService=regionReadPlatformService;
		        this.addressReadPlatformService=addressReadPlatformService;
		        this.fromJsonHelper=fromJsonHelper;
		    }
	    
	    
	    @POST
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String createService(final String apiRequestBodyAsJson) {

	        final CommandWrapper commandRequest = new CommandWrapperBuilder().createRegion().withJson(apiRequestBodyAsJson).build();
	        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	        return this.toApiJsonSerializer.serialize(result);
		}
	    
	    @GET
	    @Path("template")
	    @Consumes({ MediaType.APPLICATION_JSON })
	    @Produces({ MediaType.APPLICATION_JSON })
	    public String retrieveRegionTemplateInfo(@Context final UriInfo uriInfo) {

	   	context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
   	    List<CountryDetails> countrydata = this.addressReadPlatformService.retrieveCountries();
	    RegionData  data=new RegionData(countrydata);
	    final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	    return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
	    
	    }
	    
	    @POST
		@Path("getstates/{countryId}")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String retrieveTotalPrice(@PathParam("countryId") final Long countryId,@Context final UriInfo uriInfo,final String apiRequestBodyAsJson) {

			final JsonElement parsedQuery = this.fromJsonHelper.parse(apiRequestBodyAsJson);
	         final JsonQuery query = JsonQuery.from(apiRequestBodyAsJson, parsedQuery, this.fromJsonHelper);
	         List<StateDetails> statesData =this.regionReadPlatformService.getAvailableStates(countryId);
	         final String regionCode = fromJsonHelper.extractStringNamed("regionCode", query.parsedJson());
	         final String regionName = fromJsonHelper.extractStringNamed("regionName", query.parsedJson());
	         List<CountryDetails> countrydata = this.addressReadPlatformService.retrieveCountries();
	         RegionData regionData=new RegionData(statesData,regionCode,regionName,countryId,null);
	         regionData.setCountryDetails(countrydata);
	         final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	 		return this.toApiJsonSerializer.serialize(settings, regionData, RESPONSE_DATA_PARAMETERS);	
		}
	    
	    
	    @GET
	    @Consumes({ MediaType.APPLICATION_JSON })
	    @Produces({ MediaType.APPLICATION_JSON })
	    public String retrieveRegionsDetails(@Context final UriInfo uriInfo) {

	   	context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
   	    List<RegionData> datas= this.regionReadPlatformService.getRegionDetails();
	    final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	    return this.toApiJsonSerializer.serialize(settings, datas, RESPONSE_DATA_PARAMETERS);
	    
	    }
	    
	    @GET
	    @Path("{regionId}")
	    @Consumes({ MediaType.APPLICATION_JSON })
	    @Produces({ MediaType.APPLICATION_JSON })
	    public String retrieveSingleRegionsDetails(@PathParam("regionId") final Long regionId,@Context final UriInfo uriInfo) {
	   
	    	List<StateDetails> statesData=new ArrayList<StateDetails>();
	   	context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
   	    RegionData regionData= this.regionReadPlatformService.getSingleRegionDetails(regionId);
   	    List<RegionDetailsData> regionDetails=this.regionReadPlatformService.getRegionDetailsData(regionId);
   	   
   	    statesData =this.regionReadPlatformService.getAvailableStates(regionDetails.get(0).getCountryId());
   	  
   	    List<CountryDetails> countrydata = this.addressReadPlatformService.retrieveCountries();
   	    regionData.setCountryDetails(countrydata);
   	    regionData.setStatesData(statesData);
   	   
   	    regionData.setCountryId(regionDetails.get(0).getCountryId());
   	    
   	    
   	 int size = statesData.size();
		int selectedsize = regionDetails.size();
			for (int i = 0; i < selectedsize; i++)
  			{
				Long selected = statesData.get(i).getId();
				for (int j = 0; j < size; j++) {
					Long avialble = statesData.get(j).getId();
					if (selected == avialble) {
						statesData.remove(j);
						size--;
					}
				}
			}
			 regionData.setRegionDetails(regionDetails);
	    final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	    return this.toApiJsonSerializer.serialize(settings, regionData, RESPONSE_DATA_PARAMETERS);
	    
	    }
	    
	    @PUT
		@Path("{regionId}")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String updateRegion(@PathParam("regionId") final Long regionId,final String apiRequestBodyAsJson) {
			 final CommandWrapper commandRequest = new CommandWrapperBuilder().updateRegion(regionId).withJson(apiRequestBodyAsJson).build();
			 final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
			  return this.toApiJsonSerializer.serialize(result);
		}
	    

		 @DELETE
			@Path("{regionId}")
			@Consumes({MediaType.APPLICATION_JSON})
			@Produces({MediaType.APPLICATION_JSON})
			public String deleteRegion(@PathParam("regionId") final Long regionId) {
			 final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteregion(regionId).build();
	     final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	     return this.toApiJsonSerializer.serialize(result);

			}
		 
		 
		 

}
