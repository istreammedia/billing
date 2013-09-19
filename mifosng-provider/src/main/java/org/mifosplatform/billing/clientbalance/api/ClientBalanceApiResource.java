package org.mifosplatform.billing.clientbalance.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;
import org.mifosplatform.billing.clientbalance.service.ClientBalanceReadPlatformService;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/clientBalance")
@Component
@Scope("singleton")
public class ClientBalanceApiResource {
	
	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("balanceAmount"));
    private final String resourceNameForPermissions = "CLIENTBALANCE";
	private final PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<ClientBalanceData> toApiJsonSerializer;
   private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private ClientBalanceReadPlatformService clientBalanceReadPlatformService;
	
	@Autowired
	 public ClientBalanceApiResource(final PlatformSecurityContext context,  final DefaultToApiJsonSerializer<ClientBalanceData> toApiJsonSerializer,
	 final ApiRequestParameterHelper apiRequestParameterHelper, final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
	 final ClientBalanceReadPlatformService balanceReadPlatformService)
	{
				        this.context = context;
				        this.toApiJsonSerializer = toApiJsonSerializer;
				        this.apiRequestParameterHelper = apiRequestParameterHelper;
				        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
				        this.clientBalanceReadPlatformService=balanceReadPlatformService;
	}	
	
	@GET
    @Path("template/{clientid}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
   public String retrieveCharge(@PathParam("clientid") final Long clientid, @Context final UriInfo uriInfo)
	{
	       context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
	        ClientBalanceData balanceDatas = this.clientBalanceReadPlatformService.retrieveBalance(clientid);
	      //  ClientBalanceData balance=balanceDatas.get(0);
	        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	        return this.toApiJsonSerializer.serialize(settings, balanceDatas, RESPONSE_DATA_PARAMETERS);
	}



}
