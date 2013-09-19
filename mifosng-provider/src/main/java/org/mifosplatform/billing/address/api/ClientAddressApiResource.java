package org.mifosplatform.billing.address.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.address.data.AddressData;
import org.mifosplatform.billing.address.exception.AddressNoRecordsFoundException;
import org.mifosplatform.billing.address.service.AddressReadPlatformService;
import org.mifosplatform.billing.billingorder.exceptions.BillingOrderNoRecordsFoundException;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Path("/address")
@Component
@Scope("singleton")
public class ClientAddressApiResource {
	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("addressid","clientId",
            "addressNo","street","zipCode","city","state","country","datas","countryData","stateData","cityData","addressOptionsData"));
    private final String resourceNameForPermissions = "ADDRESS";
	  private final PlatformSecurityContext context;
	    private final DefaultToApiJsonSerializer<AddressData> toApiJsonSerializer;
	    private final ApiRequestParameterHelper apiRequestParameterHelper;
	    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	    private final AddressReadPlatformService addressReadPlatformService;

		 @Autowired
	    public ClientAddressApiResource(final PlatformSecurityContext context,final DefaultToApiJsonSerializer<AddressData> toApiJsonSerializer, final ApiRequestParameterHelper apiRequestParameterHelper,
	   final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,final AddressReadPlatformService addressReadPlatformService) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.addressReadPlatformService=addressReadPlatformService;
		    }
	@GET
    @Path("template")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAddressTemplateInfo(@Context final UriInfo uriInfo) {
        context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
    List<String> countryData = this.addressReadPlatformService.retrieveCountryDetails();
    List<String> statesData = this.addressReadPlatformService.retrieveStateDetails();
    List<String> citiesData = this.addressReadPlatformService.retrieveCityDetails();
    List<EnumOptionData> enumOptionDatas = this.addressReadPlatformService.addressType();
    AddressData data=new AddressData(null,countryData,statesData,citiesData,enumOptionDatas);
    final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
    return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
    
    }
	
	@GET
    @Path("{selectedname}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAddressdetails( @Context final UriInfo uriInfo,	@PathParam("selectedname") final String selectedname ) {
        context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
        List<AddressData> citiesData = this.addressReadPlatformService.retrieveCityDetails(selectedname);
        List<EnumOptionData> enumOptionDatas = this.addressReadPlatformService.addressType();
        AddressData data=new AddressData(citiesData, null, null, null,enumOptionDatas);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
    }
	@GET
	@Path("details/{clientId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveClientAddress( @PathParam("clientId") final Long clientId , @Context final UriInfo uriInfo) {
        context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
        List<AddressData> addressdata = this.addressReadPlatformService.retrieveAddressDetails(clientId);
        List<String> countryData = this.addressReadPlatformService.retrieveCountryDetails();
        List<String> statesData = this.addressReadPlatformService.retrieveStateDetails();
        List<String> citiesData = this.addressReadPlatformService.retrieveCityDetails();
        List<EnumOptionData> enumOptionDatas = this.addressReadPlatformService.addressType();
        AddressData data=new AddressData(addressdata,null,null,citiesData,enumOptionDatas);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
	}
	
	@GET
	@Path("template/{Name}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAddress(@PathParam("Name") final String Name , @Context final UriInfo uriInfo) {
        context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
        String city=null;
        AddressData Data = this.addressReadPlatformService.retrieveName(Name);
//        city =Data.getCity();
     
        if(Data== null){
        	throw new AddressNoRecordsFoundException();
        }
        // AddressData data=new AddressData(Data);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, Data, RESPONSE_DATA_PARAMETERS);
	}
	
	@POST
	@Path("{clientId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String createAddress(@PathParam("clientId") final Long clientId, final String apiRequestBodyAsJson) {
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().createAddress(clientId).withJson(apiRequestBodyAsJson).build();
	        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	        return this.toApiJsonSerializer.serialize(result);
	}
	/*@POST
	@Path("{clientId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response createAddress(@PathParam("clientId") final Long clientId, final String jsonRequestBody) {

		AddressCommand command = this.apiDataConversionService.convertJsonToAddressCommand(null,clientId, jsonRequestBody);

		CommandProcessingResult userId = this.addressWritePlatformService.createAddress(command);
		return Response.ok().entity(userId).build();
	}*/
	
	@PUT
	@Path("{addrId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String updateClientAddress(@PathParam("addrId") final Long addrId, final String apiRequestBodyAsJson){
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().updateAddress(addrId).withJson(apiRequestBodyAsJson).build();
		 final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		  return this.toApiJsonSerializer.serialize(result);

	}
	/*@PUT
	@Path("{addrId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateClientAddress(@PathParam("addrId") final Long addrId, final String jsonRequestBody){

		final AddressCommand command = this.apiDataConversionService.convertJsonToAddressCommand(null, addrId, jsonRequestBody);
		CommandProcessingResult entityIdentifier=this.addressWritePlatformService.updateAddress(addrId,command);
		return Response.ok().entity(entityIdentifier).build();
	}*/
	
	
	@POST
	@Path("{entityType}/new")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String NewRecord(@PathParam("entityType") final String entityType, final String jsonRequestBody) {
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().createNewRecord(entityType).withJson(jsonRequestBody).build();
	        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	        return this.toApiJsonSerializer.serialize(result);
		/* EntityTypecommand command= this.apiDataConversionService.convertJsonToEntityTypeCommand(null,jsonRequestBody);

		 CommandProcessingResult userId = this.addressWritePlatformService.createNewRecord(command,entityType);
		return Response.ok().entity(userId).build();*/
	}
}
