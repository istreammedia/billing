package org.mifosplatform.billing.batchjob.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;


import org.mifosplatform.billing.batchjob.data.BatchJobData;
import org.mifosplatform.billing.batchjob.service.BatchJobReadPlatformService;
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


@Path("/batchs")
@Component
@Scope("singleton")
public class BatchJobApiResource {
	
	private String resourceType = "BATCH";
	private static final Set<String> SUPPORTED_RESPONSE_PARAMETERS = new HashSet<String>(Arrays.asList("id","batchName","query"));
	
	
	private PlatformSecurityContext context; 
	private PortfolioCommandSourceWritePlatformService portfolioCommandSourceWritePlatformService;
	private DefaultToApiJsonSerializer<BatchJobData> apiJsonSerializer;
	private BatchJobReadPlatformService batchJobReadPlatformService; 
	private ApiRequestParameterHelper apiRequestParameterHelper;
	
	@Autowired
	public BatchJobApiResource(final PlatformSecurityContext context, final PortfolioCommandSourceWritePlatformService portfolioCommandSourceWritePlatformService, final DefaultToApiJsonSerializer<BatchJobData> apiJsonSerializer, final BatchJobReadPlatformService batchJobReadPlatformService, ApiRequestParameterHelper apiRequestParameterHelper) {
		this.context = context;
		this.portfolioCommandSourceWritePlatformService = portfolioCommandSourceWritePlatformService;
		this.apiJsonSerializer = apiJsonSerializer;
		this.batchJobReadPlatformService = batchJobReadPlatformService;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		
	}
	
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String createBatch(final String requestBody){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		CommandWrapper command = new CommandWrapperBuilder().createBatch().withJson(requestBody).build();
		final CommandProcessingResult result = portfolioCommandSourceWritePlatformService.logCommandSource(command);
		return apiJsonSerializer.serialize(result);
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String retriveBatchJobs(@Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		List<BatchJobData> batchJob = batchJobReadPlatformService.retriveBatchDetails();
		ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return apiJsonSerializer.serialize(settings, batchJob, SUPPORTED_RESPONSE_PARAMETERS);
	}
	
	
	@GET
	@Path("template")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String retriveBatchJobTemplate(@Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		BatchJobData batchJob = new BatchJobData();
		ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return apiJsonSerializer.serialize(settings, batchJob, SUPPORTED_RESPONSE_PARAMETERS);
	}
}
