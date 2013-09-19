package org.mifosplatform.billing.epgprogramguide.api;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.epgprogramguide.data.EpgProgramGuideData;
import org.mifosplatform.billing.epgprogramguide.service.EpgProgramGuideReadPlatformService;
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

@Path("/epgprogramguide")
@Component
@Scope("singleton")
public class EpgProgramGuideApiResource {
	  private  final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("id",
	           "channelName","channelIcon","programDate","startTime","stopTime","programTitle","programDescription","type","genre"));
        private final String resourceNameForPermissions = "EPGPROGRAMGUIDE";
	    private final PlatformSecurityContext context;
	    private final DefaultToApiJsonSerializer<EpgProgramGuideData> toApiJsonSerializer;
	    private final ApiRequestParameterHelper apiRequestParameterHelper;
	    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	    private final EpgProgramGuideReadPlatformService epgProgramGuideReadPlatformService;
	    
	    @Autowired
	    public EpgProgramGuideApiResource(final PlatformSecurityContext context, 
	    final DefaultToApiJsonSerializer<EpgProgramGuideData> toApiJsonSerializer, final ApiRequestParameterHelper apiRequestParameterHelper,
	    final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,final EpgProgramGuideReadPlatformService epgProgramGuideReadPlatformService) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.epgProgramGuideReadPlatformService = epgProgramGuideReadPlatformService;
		    }		
		
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String createSubscription(final String apiRequestBodyAsJson) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final CommandWrapper commandRequest = new CommandWrapperBuilder().createEpg().withJson(apiRequestBodyAsJson).build();
	    final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	    return this.toApiJsonSerializer.serialize(result);
	}

	
	@GET
	@Path("{channelName}/{counter}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retrieveEpgDetails(@PathParam("channelName") final String channelName,@PathParam("counter") final Long counter, @Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		List<EpgProgramGuideData> epgProgramGuideDatas = epgProgramGuideReadPlatformService.retrivePrograms(channelName,counter);
		EpgProgramGuideData programGuideData = new EpgProgramGuideData(epgProgramGuideDatas);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	    return this.toApiJsonSerializer.serialize(settings, programGuideData, RESPONSE_DATA_PARAMETERS);
	}
}