package org.mifosplatform.billing.inventory.mrn.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.inventory.mrn.data.InventoryTransactionHistoryData;
import org.mifosplatform.billing.inventory.mrn.data.MRNDetailsData;
import org.mifosplatform.billing.inventory.mrn.service.MRNDetailsReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.ToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.office.data.OfficeData;
import org.mifosplatform.organisation.office.service.OfficeReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("singleton")
@Path("/mrn")
public class MRNDetailsApiResource {

	
	
	private final Set<String> RESPONSE_PARAMETERS = new HashSet<String>(Arrays.asList("mrnId","requestedDate","itemDescription","fromOffice","toOffice","orderdQuantity","receivedQuantity","status","officeId","officeName","parentId","movedDate"));
	private final Set<String> RESPONSE_PARAMETERS_HISTORY = new HashSet<String>(Arrays.asList("mrnId","transactionDate","itemDescription","fromOffice","toOffice","serialNumber","orderdQuantity","movedQuantity","movement"));
	private final static String resourceType = "MRNDETAILS";
	
	final private PlatformSecurityContext context;
	final private PortfolioCommandSourceWritePlatformService portfolioCommandSourceWritePlatformService;
	final private MRNDetailsReadPlatformService mrnDetailsReadPlatformService;
	final private ApiRequestParameterHelper apiRequestParameterHelper;
	final private ToApiJsonSerializer<MRNDetailsData> apiJsonSerializer;
	final private ToApiJsonSerializer<InventoryTransactionHistoryData> apiJsonSerializerForData;
	private final OfficeReadPlatformService officeReadPlatformService;
	
	@Autowired
	public MRNDetailsApiResource(final PlatformSecurityContext context, final PortfolioCommandSourceWritePlatformService portfolioCommandSourceWritePlatformService
			,final MRNDetailsReadPlatformService mrnDetailsReadPlatformService, final ApiRequestParameterHelper apiRequestParameterHelper 
			,final ToApiJsonSerializer<MRNDetailsData> apiJsonSerializer
			,final OfficeReadPlatformService officeReadPlatformService
			,final ToApiJsonSerializer<InventoryTransactionHistoryData> apiJsonSerializerForData) {
		this.context = context;
		this.portfolioCommandSourceWritePlatformService = portfolioCommandSourceWritePlatformService;
		this.mrnDetailsReadPlatformService = mrnDetailsReadPlatformService;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.apiJsonSerializer =  apiJsonSerializer;
		this.officeReadPlatformService = officeReadPlatformService;
		this.apiJsonSerializerForData = apiJsonSerializerForData;
	}
	
	
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveMRNDetails(@Context final UriInfo uriInfo ){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		final List<MRNDetailsData> mrnDetailsDatas = mrnDetailsReadPlatformService.retriveMRNDetails();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return apiJsonSerializer.serialize(settings,mrnDetailsDatas,RESPONSE_PARAMETERS);
	}
	
	@GET
	@Path("template")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String retriveMrnTemplate(@Context UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		//final List<MRNDetailsData> mrnDetailsTemplateData = mrnDetailsReadPlatformService.retriveMrnDetailsTemplate();
		final Collection<OfficeData> officeData = this.officeReadPlatformService.retrieveAllOfficesForDropdown();
		final Collection<MRNDetailsData> itemMasterData = this.mrnDetailsReadPlatformService.retriveItemMasterDetails();
		final MRNDetailsData mrnDetailsData = new MRNDetailsData(officeData,itemMasterData);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return apiJsonSerializer.serialize(settings,mrnDetailsData,RESPONSE_PARAMETERS);
	}
	
	@Path("template/ids")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String retriveSerialNumbers(@Context final UriInfo uriInfo, @QueryParam("mrnId") final Long mrnId){
		context.authenticatedUser();
		if(mrnId!=null && mrnId > 0){
			final MRNDetailsData mrnDetails = mrnDetailsReadPlatformService.retriveFromAndToOffice(mrnId);
			final List<String> serialNumber = mrnDetailsReadPlatformService.retriveSerialNumbers(mrnDetails.getFromOfficeNum(),mrnId);
			final MRNDetailsData mrnDetailsData = new MRNDetailsData(serialNumber);
			final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
			return apiJsonSerializer.serialize(settings,mrnDetailsData,RESPONSE_PARAMETERS);
		}
		final Collection<MRNDetailsData> mrnIds = mrnDetailsReadPlatformService.retriveMrnIds();
		final MRNDetailsData mrnDetailsData = new MRNDetailsData(mrnIds);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return apiJsonSerializer.serialize(settings,mrnDetailsData,RESPONSE_PARAMETERS);
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String createMRN(final String jsonRequestBody){
		final CommandWrapper command = new CommandWrapperBuilder().createMRN().withJson(jsonRequestBody).build();
		final CommandProcessingResult result = portfolioCommandSourceWritePlatformService.logCommandSource(command);
		return apiJsonSerializer.serialize(result);
	}
	
	@Path("movemrn")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String moveMRN(final String jsonRequestBody){
		final CommandWrapper command = new CommandWrapperBuilder().moveMRN().withJson(jsonRequestBody).build();
		final CommandProcessingResult result = portfolioCommandSourceWritePlatformService.logCommandSource(command);
		return apiJsonSerializer.serialize(result);
	}
	
	@GET
	@Path("/history")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveMMRNHistory(@Context final UriInfo uriInfo){
		
		context.authenticatedUser().validateHasReadPermission(resourceType);
		final List<InventoryTransactionHistoryData> mrnDetailsDatas = mrnDetailsReadPlatformService.retriveHistory();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return apiJsonSerializerForData.serialize(settings,mrnDetailsDatas,RESPONSE_PARAMETERS_HISTORY);
	
	}
	
}
