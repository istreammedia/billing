package org.mifosplatform.billing.eventorder.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.mifosplatform.billing.eventorder.data.EventOrderData;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/eventorder")
@Component
@Scope("singleton")
public class EventOrderApiResource {
	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("eventId","eventBookedDate","eventValidDate","clientId",
			   "eventPriceId","movieLocation","bookedPrice","eventStatus","chargeCode"));
    private final String resourceNameForPermissions = "EVENTORDER";
    private final PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<EventOrderData> toApiJsonSerializer;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private final FromJsonHelper fromJsonHelper;
		 @Autowired
	    public EventOrderApiResource(final PlatformSecurityContext context,final DefaultToApiJsonSerializer<EventOrderData> toApiJsonSerializer,
	           final ApiRequestParameterHelper apiRequestParameterHelper,final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
	           final FromJsonHelper fromJsonHelper) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
                this.fromJsonHelper=fromJsonHelper;
		 }		
		 
@POST
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public String createNewEventOrder(@PathParam("clientId") final Long clientId,final String apiRequestBodyAsJson) {
	 final CommandWrapper commandRequest = new CommandWrapperBuilder().createEventOrder().withJson(apiRequestBodyAsJson).build();
    final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
    return this.toApiJsonSerializer.serialize(result);
	}		

}
