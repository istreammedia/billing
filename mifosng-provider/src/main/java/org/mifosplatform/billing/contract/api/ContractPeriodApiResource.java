package org.mifosplatform.billing.contract.api;

import java.util.Arrays;
import java.util.Collection;
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

import org.mifosplatform.billing.contract.data.PeriodData;
import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.billing.contract.service.ContractPeriodReadPlatformService;
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

@Path("/subscriptions")
@Component
@Scope("singleton")
public class ContractPeriodApiResource {
	  private  final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("id",
	           "subscriptionPeriod","subscriptionType","units","allowedtypes","subscriptionTypeId"));
        private final String resourceNameForPermissions = "CONTRACT";
	    private final PlatformSecurityContext context;
	    private final DefaultToApiJsonSerializer<SubscriptionData> toApiJsonSerializer;
	    private final ApiRequestParameterHelper apiRequestParameterHelper;
	    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	    private ContractPeriodReadPlatformService contractPeriodReadPlatformService;
	    
	    @Autowired
	    public ContractPeriodApiResource(final PlatformSecurityContext context, 
	    final DefaultToApiJsonSerializer<SubscriptionData> toApiJsonSerializer, final ApiRequestParameterHelper apiRequestParameterHelper,
	    final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,final ContractPeriodReadPlatformService contractPeriodReadPlatformService) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.contractPeriodReadPlatformService=contractPeriodReadPlatformService;
		    }		
		
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String createNewContract(final String apiRequestBodyAsJson) {
		  final CommandWrapper commandRequest = new CommandWrapperBuilder().createContract().withJson(apiRequestBodyAsJson).build();
	        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	        return this.toApiJsonSerializer.serialize(result);
	}

	
	@GET
	@Path("{SubscriptionId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retrieveContractDetails(@PathParam("SubscriptionId") final Long SubscriptionId, @Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		SubscriptionData productData = this.contractPeriodReadPlatformService.retrieveSubscriptionData(SubscriptionId);
		 final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		 List<PeriodData> allowedtypes = this.contractPeriodReadPlatformService.retrieveAllPlatformPeriod();
			productData = new SubscriptionData(allowedtypes, productData);
	        return this.toApiJsonSerializer.serialize(settings, productData, RESPONSE_DATA_PARAMETERS);
	}
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retrieveAllContracts(@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		Collection<SubscriptionData> products=this.contractPeriodReadPlatformService.retrieveAllSubscription();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, products, RESPONSE_DATA_PARAMETERS);
	}
	
	@PUT
	@Path("{SubscriptionId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String updateSubscription(@PathParam("SubscriptionId") final Long contractId, final String apiRequestBodyAsJson){

		
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().updateContract(contractId).withJson(apiRequestBodyAsJson).build();
		 final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		  return this.toApiJsonSerializer.serialize(result);
	}
	
	 @DELETE
		@Path("{SubscriptionId}")
		@Consumes({MediaType.APPLICATION_JSON})
		@Produces({MediaType.APPLICATION_JSON})
		public String deleteSubscription(@PathParam("SubscriptionId") final Long SubscriptionId) {
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteContract(SubscriptionId).build();

	        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

	        return this.toApiJsonSerializer.serialize(result);

		}
	 @GET
		@Path("template")
		@Consumes({MediaType.APPLICATION_JSON})
		@Produces({MediaType.APPLICATION_JSON})
		public String retrieveAllSubscriptionDetails(@Context final UriInfo uriInfo) {

		 context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
			
			List<PeriodData> allowedtypes = this.contractPeriodReadPlatformService.retrieveAllPlatformPeriod();
			final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters()); 
			SubscriptionData product = new SubscriptionData(allowedtypes);
	         return this.toApiJsonSerializer.serialize(settings, product, RESPONSE_DATA_PARAMETERS);
		}
	 

}
