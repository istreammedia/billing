package org.mifosplatform.billing.ownedhardware.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.batchjob.data.BatchJobData;
import org.mifosplatform.billing.batchjob.service.BatchJobReadPlatformService;
import org.mifosplatform.billing.clientprospect.data.ProspectStatusRemarkData;
import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.mcodevalues.data.MCodeData;
import org.mifosplatform.billing.ownedhardware.data.OwnedHardwareData;
import org.mifosplatform.billing.ownedhardware.service.OwnedHardwareReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Path("ownedhardware")
@Component
@Scope("singleton")
public class OwnedHardwareApiResource {

	private String resourceType = "OWNEDHARDWARE";
	
	private static final Set<String> SUPPORTED_RESPONSE_PARAMETERS = new HashSet<String>(Arrays.asList("id","clientId","serialNumber","provisioningSerialNumber","status","itemType","allocationDate","locale","dateFormat"));
	private static final Set<String> SUPPORTED_RESPONSE_PARAMETERS_ITEMCODE = new HashSet<String>(Arrays.asList("id","itemDescription"));
	
	private PlatformSecurityContext context; 
	private PortfolioCommandSourceWritePlatformService portfolioCommandSourceWritePlatformService;
	private DefaultToApiJsonSerializer<OwnedHardwareData> apiJsonSerializer;
	private DefaultToApiJsonSerializer<ItemData> apiJsonSerializerForItemCode;
	private OwnedHardwareReadPlatformService ownedHardwareReadPlatformService; 
	private ApiRequestParameterHelper apiRequestParameterHelper;
	
	
	@Autowired
	public OwnedHardwareApiResource(final PlatformSecurityContext context, final PortfolioCommandSourceWritePlatformService portfolioCommandSourceWritePlatformService, final DefaultToApiJsonSerializer<OwnedHardwareData> apiJsonSerializer, final OwnedHardwareReadPlatformService ownedHardwareReadPlatformService, final ApiRequestParameterHelper apiRequestParameterHelper,DefaultToApiJsonSerializer<ItemData> apiJsonSerializerForItemCode) {
		this.context = context;
		this.portfolioCommandSourceWritePlatformService = portfolioCommandSourceWritePlatformService;
		this.apiJsonSerializer = apiJsonSerializer;
		this.ownedHardwareReadPlatformService = ownedHardwareReadPlatformService;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.apiJsonSerializerForItemCode = apiJsonSerializerForItemCode;
	}
	
	
	@GET
	@Path("{clientId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveOwnedHardwareData(@PathParam("clientId") final Long clientId, @Context final UriInfo uriInfo){
		this.context.authenticatedUser().validateHasReadPermission(resourceType);
		List<OwnedHardwareData> ownedHardwareDatas = ownedHardwareReadPlatformService.retriveOwnedHardwareData(clientId);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.apiJsonSerializer.serialize(settings,ownedHardwareDatas,SUPPORTED_RESPONSE_PARAMETERS);
	}
	
	@POST
	@Path("{clientId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String createOwnedHardware(@PathParam("clientId") final Long clientId,final String JsonRequestBody){
		
		CommandWrapper command = new CommandWrapperBuilder().createOwnedHardware(clientId).withJson(JsonRequestBody).build();
		final CommandProcessingResult result = portfolioCommandSourceWritePlatformService.logCommandSource(command);
		return apiJsonSerializer.serialize(result);
	}
	
	@GET
	@Path("template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveOwnedHardwareTemplate(@Context final UriInfo uriInfo){
		this.context.authenticatedUser().validateHasReadPermission(resourceType);
		List<ItemData> itemCodes = ownedHardwareReadPlatformService.retriveTemplate();
		ItemData itemCode = new ItemData(itemCodes);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.apiJsonSerializerForItemCode.serialize(settings,itemCode,SUPPORTED_RESPONSE_PARAMETERS_ITEMCODE);
	}
}
