package org.mifosplatform.billing.entitlements.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.entitlements.data.EntitlementsData;
import org.mifosplatform.billing.entitlements.service.EntitlementReadPlatformService;
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


@Path("/entitlements")
@Component
@Scope("singleton")
public class EntitlementsApiResource {

	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("balanceAmount"));
    private final String resourceNameForPermissions = "CLIENTBALANCE";
	private final PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<EntitlementsData> toApiJsonSerializer;
   private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private EntitlementReadPlatformService entitlementReadPlatformService;
	
	@Autowired
	 public EntitlementsApiResource(final PlatformSecurityContext context,  final DefaultToApiJsonSerializer<EntitlementsData> toApiJsonSerializer,
	 final ApiRequestParameterHelper apiRequestParameterHelper, final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
	 final EntitlementReadPlatformService comvenientReadPlatformService)
	{
				        this.context = context;
				        this.toApiJsonSerializer = toApiJsonSerializer;
				        this.apiRequestParameterHelper = apiRequestParameterHelper;
				        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
				        this.entitlementReadPlatformService=comvenientReadPlatformService;
	}	
	
	@GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
   public String retrievedata(@QueryParam("no") final Long No,	@Context final UriInfo uriInfo)
	{
	       context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
	        List<EntitlementsData> data=this.entitlementReadPlatformService.getProcessingData(No);
	        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	        return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
	}
	
	@POST
	@Path("{msgId}")
	@Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
	public String insertdata(@PathParam("msgId") final Long id,final String apiRequestBodyAsJson) {

		   final CommandWrapper commandRequest=new CommandWrapperBuilder().createEntitlement(id).withJson(apiRequestBodyAsJson).build();
		   final CommandProcessingResult result=this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		   return this.toApiJsonSerializer.serialize(result);
		}

	
	
	
}
