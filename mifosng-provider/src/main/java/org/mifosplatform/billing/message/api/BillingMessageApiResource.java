package org.mifosplatform.billing.message.api;

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

import org.mifosplatform.billing.message.data.BillingMessageData;
import org.mifosplatform.billing.message.service.BillingMesssageReadPlatformService;
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



@Path("/messages")
@Component
@Scope("singleton")
public class BillingMessageApiResource {
	
	private final Set<String> MESSAGE_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("id", "templateDescription", 
			"subject", "header", "body","footer","messageParams","deleteButtonId"));
	
	 private final String resourceNameForPermissions = "MESSAGE";
	
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private final DefaultToApiJsonSerializer<BillingMessageData> toApiJsonSerializer;
	private final BillingMesssageReadPlatformService billingMesssageReadPlatformService;
	private final PlatformSecurityContext context;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	
	
	
	
	@Autowired
	public BillingMessageApiResource(final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,DefaultToApiJsonSerializer<BillingMessageData> toApiJsonSerializer
			,BillingMesssageReadPlatformService billingMesssageReadPlatformService,PlatformSecurityContext context
			,ApiRequestParameterHelper apiRequestParameterHelper)
	{
		this.commandsSourceWritePlatformService=commandsSourceWritePlatformService;
		this.toApiJsonSerializer=toApiJsonSerializer;
		this.context=context;
		this.apiRequestParameterHelper=apiRequestParameterHelper;
		this.billingMesssageReadPlatformService=billingMesssageReadPlatformService;
		
	}
	
	@POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createCode(final String apiRequestBodyAsJson) {
		
        final CommandWrapper commandRequest = new CommandWrapperBuilder().createBillingMessage().withJson(apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.toApiJsonSerializer.serialize(result);
    } 
	
	@POST
	@Path("{messageId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String addData(@PathParam("messageId") final Long messageId,final String apiRequestBodyAsJson) {
		
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().createMessageData(messageId).withJson(apiRequestBodyAsJson).build();

	        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

	        return this.toApiJsonSerializer.serialize(result);
	}
	
	
	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	 public String retrieveAllMessageTemplate(@Context final UriInfo uriInfo) {
		
        context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
        final Collection<BillingMessageData> templateData = this.billingMesssageReadPlatformService.retrieveAllMessageTemplates();
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, templateData, MESSAGE_DATA_PARAMETERS);
    }
	
	@GET
	@Path("template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	 public String retrieveTemplate(@Context final UriInfo uriInfo) {
		
        context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
        final Collection<BillingMessageData> templateData = this.billingMesssageReadPlatformService.retrieveAllMessageTemplateParams();
         
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, templateData, MESSAGE_DATA_PARAMETERS);
    }
	
	@GET
	@Path("{messageId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
    public String retrieveSingleMessageTemplate(@PathParam("messageId") final Long messageId,@Context final UriInfo uriInfo) {
		
        context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
        final BillingMessageData templateData = this.billingMesssageReadPlatformService.retrieveMessageTemplate(messageId);
        List<BillingMessageData> messageParams=this.billingMesssageReadPlatformService.retrieveMessageParams(messageId);
                  templateData.setMessageParams(messageParams);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, templateData, MESSAGE_DATA_PARAMETERS);
	
	}
	
	@PUT
	@Path("{messageId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String templateEditData(@PathParam("messageId") final Long messageId,final String apiRequestBodyAsJson) {
		
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().updateMessageData(messageId).withJson(apiRequestBodyAsJson).build();

	        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

	        return this.toApiJsonSerializer.serialize(result);
	}
	
	@DELETE
	@Path("{messageId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteEventPricing(@PathParam("messageId") final Long messageId) {
		final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteMessageData(messageId).build();
		final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		
		return this.toApiJsonSerializer.serialize(result);
	}
	
}
