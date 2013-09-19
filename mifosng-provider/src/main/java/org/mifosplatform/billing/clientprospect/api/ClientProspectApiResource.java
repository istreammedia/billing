package org.mifosplatform.billing.clientprospect.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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

import org.mifosplatform.billing.address.service.AddressReadPlatformService;
import org.mifosplatform.billing.clientprospect.data.ClientProspectData;
import org.mifosplatform.billing.clientprospect.data.ProspectDetailAssignedToData;
import org.mifosplatform.billing.clientprospect.data.ProspectDetailCallStatus;
import org.mifosplatform.billing.clientprospect.data.ProspectDetailData;
import org.mifosplatform.billing.clientprospect.data.ProspectPlanCodeData;
import org.mifosplatform.billing.clientprospect.data.ProspectStatusRemarkData;
import org.mifosplatform.billing.clientprospect.service.ClientProspectReadPlatformService;
import org.mifosplatform.billing.mcodevalues.data.MCodeData;
import org.mifosplatform.billing.mcodevalues.service.MCodeReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.ToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Path("/prospects")
@Component
@Scope("singleton")
public class ClientProspectApiResource {

	private final String resourceType = "PROSPECT";
	
	final private PlatformSecurityContext context;
	final private PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService;
	final private ClientProspectReadPlatformService clientProspectReadPlatformService;
	final private ApiRequestParameterHelper apiRequestParameterHelper;
	final private MCodeReadPlatformService codeReadPlatformService;
	final private AddressReadPlatformService addressReadPlatformService;
	
	final private Set<String> PROSPECT_RESPONSE_DATA_PARAMETER = new HashSet<String>(Arrays.asList("id","type","firstName","middleName","lastName","homePhoneNumber","workPhoneNumber","mobileNumber","email","sourceOfPublicity","preferredCallingTime","note","address","area","district","region","city","plan","status","callStatus","assignedTo","notes","zipCode"));
	final private Set<String> PROSPECTDETAIL_RESPONSE_DATA_PARAMETER = new HashSet<String>(Arrays.asList("prospectId","callStatus","assignedTo","notes","locale"));
	final private Set<String> PROSPECTDETAILREMARK_RESPONSE_DATA_PARAMETER = new HashSet<String>(Arrays.asList("statusRemark","statusRemarkId"));
	
	final private ToApiJsonSerializer<ClientProspectData> apiJsonSerializer;
	final private ToApiJsonSerializer<ProspectDetailData> apiJsonSerializerForProspectDetail;
	final private ToApiJsonSerializer<ProspectStatusRemarkData> apiJsonSerializerForStatusRemark;
	
	
	@Autowired
	public ClientProspectApiResource(final PlatformSecurityContext context, final PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService, final ToApiJsonSerializer<ClientProspectData> apiJsonSerializer, final ClientProspectReadPlatformService clientProspectReadPlatformService, final ApiRequestParameterHelper apiRequestParameterHelper, final MCodeReadPlatformService codeReadPlatformService, final ToApiJsonSerializer<ProspectDetailData> apiJsonSerializerForProspectDetail, final ToApiJsonSerializer<ProspectStatusRemarkData> apiJsonSerializerForStatusRemark, final AddressReadPlatformService addressReadPlatformService) {
		this.context = context;	
		this.commandSourceWritePlatformService = commandSourceWritePlatformService;
		this.clientProspectReadPlatformService = clientProspectReadPlatformService;
		this.apiJsonSerializer = apiJsonSerializer;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.codeReadPlatformService = codeReadPlatformService;
		this.apiJsonSerializerForProspectDetail = apiJsonSerializerForProspectDetail;
		this.apiJsonSerializerForStatusRemark = apiJsonSerializerForStatusRemark;
		this.addressReadPlatformService = addressReadPlatformService;
	}
	
	
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String retriveProspects(@Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		Collection<ClientProspectData> clientProspectData = null;	
		//Collection<MCodeData> sourceOfPublicityData = codeReadPlatformService.getCodeValue("Source Type");
		clientProspectData = this.clientProspectReadPlatformService.retriveClientProspect();
		//clientProspectData.setSourceOfPublicityData(sourceOfPublicityData);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.apiJsonSerializer.serialize(settings, clientProspectData,PROSPECT_RESPONSE_DATA_PARAMETER);
	}
	
	@GET
	@Path("{clientProspectId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String retriveProspects(@Context final UriInfo uriInfo, @QueryParam("clientProspectId") final Long clientProspectId){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		ProspectDetailData clientProspectData = null;	
		clientProspectData = this.clientProspectReadPlatformService.retriveClientProspect(clientProspectId);
		Collection<MCodeData> mCodeData = codeReadPlatformService.getCodeValue("Call Status");
		List<ProspectDetailCallStatus> callStatusData = new ArrayList<ProspectDetailCallStatus>();
		List<ProspectDetailAssignedToData> assignedToData = clientProspectReadPlatformService.retrieveUsers();
		for(MCodeData code:mCodeData){
			ProspectDetailCallStatus p = new ProspectDetailCallStatus(code.getId(), code.getmCodeValue());
			callStatusData.add(p);
		}	
		clientProspectData.setCallStatusData(callStatusData);
		clientProspectData.setAssignedToData(assignedToData);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.apiJsonSerializerForProspectDetail.serialize(settings, clientProspectData,PROSPECT_RESPONSE_DATA_PARAMETER);
	}
		
	@GET
	@Path("cancel/{prospectId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveDataForCancle(@Context final UriInfo uriInfo, @QueryParam("prospectId") final Long prospectId){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		Collection<MCodeData> mCodeData = codeReadPlatformService.getCodeValue("Status Remark");
		List<ProspectStatusRemarkData> statusRemarkData = new ArrayList<ProspectStatusRemarkData>();
		for(MCodeData codeData:mCodeData){
			statusRemarkData.add(new ProspectStatusRemarkData(codeData.getId(), codeData.getmCodeValue()));
		}
		ProspectStatusRemarkData data = new ProspectStatusRemarkData();
		data.setStatusRemarkData(statusRemarkData);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.apiJsonSerializerForStatusRemark.serialize(settings,data ,PROSPECTDETAILREMARK_RESPONSE_DATA_PARAMETER);
		
	}
	

	@GET
	@Path("template")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String retriveProspectsTemplate(@Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		ClientProspectData clientProspectData = null;	
		Collection<MCodeData> sourceOfPublicityData = codeReadPlatformService.getCodeValue("Source Type");
		clientProspectData = new ClientProspectData();//.clientProspectReadPlatformService.retriveClientProspectTemplate();
		Collection<ProspectPlanCodeData> planData = clientProspectReadPlatformService.retrivePlans();
		clientProspectData.setPlanData(planData);
		clientProspectData.setSourceOfPublicityData(sourceOfPublicityData);
		clientProspectData.setStatus("New");
		
		List<String> countryData = this.addressReadPlatformService.retrieveCountryDetails();
	    List<String> statesData = this.addressReadPlatformService.retrieveStateDetails();
	    List<String> citiesData = this.addressReadPlatformService.retrieveCityDetails();
		clientProspectData.setCountryData(countryData);
		clientProspectData.setStateData(statesData);
		clientProspectData.setCityData(citiesData);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.apiJsonSerializer.serialize(settings, clientProspectData,PROSPECT_RESPONSE_DATA_PARAMETER);
	}
	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String createProspects(final String jsonRequestBody){
	
		CommandWrapper commandRequest = new CommandWrapperBuilder().createProspect().withJson(jsonRequestBody).build();
		final CommandProcessingResult result = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		return apiJsonSerializer.serialize(result);
	}
	
	@POST
	@Path("converttoclient/{deleteProspectId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String convertProspecttoClient(@PathParam("deleteProspectId") final Long deleteProspectId, final String jsonRequestBody){
		
		CommandWrapper commandRequest = new CommandWrapperBuilder().convertProspectToClient(deleteProspectId).build();
		final CommandProcessingResult result = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		return apiJsonSerializer.serialize(result);
	}
	
	
	@PUT
	@Path("{prospectId}/template")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String updateProspect(@PathParam("prospectId") final Long prospectId,final String jasonRequestBody){
		
		CommandWrapper commandRequest = new CommandWrapperBuilder().updateProspect(prospectId).withJson(jasonRequestBody).build();
		final CommandProcessingResult result = commandSourceWritePlatformService.logCommandSource(commandRequest);
		return apiJsonSerializer.serialize(result);
	}
	
	@PUT
	@Path("{deleteProspectId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String deleteProspect(@PathParam("deleteProspectId") final Long deleteProspectId, final String jsonRequestBody){
		
		CommandWrapper commandRequest = new CommandWrapperBuilder().deleteProspect(deleteProspectId).withJson(jsonRequestBody).build();
		final CommandProcessingResult result = commandSourceWritePlatformService.logCommandSource(commandRequest);
		return apiJsonSerializer.serialize(result);
	}
	
	@GET
	@Path("{prospectdetailid}/history")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String history(@Context final UriInfo uriInfo, @PathParam("prospectdetailid") final Long prospectdetailid){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		List<ProspectDetailData> prospectDetailData = this.clientProspectReadPlatformService.retriveProspectDetailHistory(prospectdetailid);
		ProspectDetailData data = new ProspectDetailData(prospectDetailData);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.apiJsonSerializerForProspectDetail.serialize(settings, data,PROSPECT_RESPONSE_DATA_PARAMETER);
	}
	
	
}
