package org.mifosplatform.billing.batchjobscheduling.api;

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

import org.mifosplatform.billing.batchjobscheduling.data.BatchJobSchedulingData;
import org.mifosplatform.billing.batchjobscheduling.data.BatchNameData;
import org.mifosplatform.billing.batchjobscheduling.data.MessageTemplateData;
import org.mifosplatform.billing.batchjobscheduling.data.ProcessTypeData;
import org.mifosplatform.billing.batchjobscheduling.data.ScheduleTypeData;
import org.mifosplatform.billing.batchjobscheduling.service.BatchJobSchedulingReadPlatformService;
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



@Path("/jobschedules")
@Component
@Scope("singleton")
public class BatchJobSchedulingApiResource {

	private static final Set<String> SUPPORTED_RESPONSE_PARAMETERS = new HashSet<String>(Arrays.asList("id","batchName","process","msgTemplateDescription","scheduleType","scheduleTime","status"));
	private static final String resourceType = "SCHEDULE";
	
	private DefaultToApiJsonSerializer<BatchJobSchedulingData> toApiJsonSerializer;
	private ApiRequestParameterHelper apiRequestParameterHelper;
	private BatchJobSchedulingReadPlatformService batchJobSchedulingReadPlatformService;
	private PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService;
	private PlatformSecurityContext context;
	
	@Autowired
	public BatchJobSchedulingApiResource(final PlatformSecurityContext context,	final DefaultToApiJsonSerializer<BatchJobSchedulingData> toApiJsonSerializer, final ApiRequestParameterHelper apiRequestParameterHelper, final BatchJobSchedulingReadPlatformService batchJobSchedulingReadPlatformService, final PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService) {
		this.context = context;
		this.toApiJsonSerializer = toApiJsonSerializer;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.batchJobSchedulingReadPlatformService = batchJobSchedulingReadPlatformService;
		this.commandSourceWritePlatformService = commandSourceWritePlatformService;
	}
	
	
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveAllSchedulingData(@Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		final List<BatchJobSchedulingData> schedullingData = batchJobSchedulingReadPlatformService.retriveAllData();
		final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return toApiJsonSerializer.serialize(settings,schedullingData,SUPPORTED_RESPONSE_PARAMETERS);
	}
	
	
	@GET
	@Path("template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveAllSchedulingDataTemplate(@Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		
		final List<BatchNameData> batchNameData = batchJobSchedulingReadPlatformService.retriveBatchNames();
		final List<ProcessTypeData> processTypeData = batchJobSchedulingReadPlatformService.retriveProcessTypes();
		final List<ScheduleTypeData> scheduleTypeData = batchJobSchedulingReadPlatformService.retriveScheduleTypes(); 
		final List<MessageTemplateData> messageTemplateData = batchJobSchedulingReadPlatformService.retriveMessageTemplates();
		final BatchJobSchedulingData batchJobSchedulingData = new BatchJobSchedulingData(batchNameData, processTypeData,scheduleTypeData,messageTemplateData);

		batchJobSchedulingData.setStatus("N");
		final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return toApiJsonSerializer.serialize(settings,batchJobSchedulingData,SUPPORTED_RESPONSE_PARAMETERS);
	}
	
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String createSchedule(final String jsonRequestBody){
		
		context.authenticatedUser().validateHasReadPermission(resourceType);
		CommandWrapper command = new CommandWrapperBuilder().createSchedule().withJson(jsonRequestBody).build();
		final CommandProcessingResult result = commandSourceWritePlatformService.logCommandSource(command);
		return this.toApiJsonSerializer.serialize(result);
	}
}





