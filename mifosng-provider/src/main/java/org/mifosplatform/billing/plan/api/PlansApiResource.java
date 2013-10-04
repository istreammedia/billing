package org.mifosplatform.billing.plan.api;

import java.util.Arrays;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.billing.plan.data.BillRuleData;
import org.mifosplatform.billing.plan.data.PlanData;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.plan.data.SystemData;
import org.mifosplatform.billing.plan.service.PlanReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/plans")
@Component
@Scope("singleton")
public class PlansApiResource {

	private  final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("id", "planCode", "plan_description", "startDate",
            "endDate", "status", "service_code", "service_description","Period", "charge_code", "charge_description","servicedata","contractPeriod","provisionSystem",
            "service_type", "charge_type", "allowedtypes","selectedservice","bill_rule","billiingcycle","servicedata","services","statusname","planstatus","volumeTypes"));
	 private final String resourceNameForPermissions = "PLAN";
	  private final PlatformSecurityContext context;
	    private final DefaultToApiJsonSerializer<PlanData> toApiJsonSerializer;
	    private final ApiRequestParameterHelper apiRequestParameterHelper;
	    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	    private final PlanReadPlatformService planReadPlatformService;
	    
	    @Autowired
	    public PlansApiResource(final PlatformSecurityContext context, 
	   final DefaultToApiJsonSerializer<PlanData> toApiJsonSerializer, final ApiRequestParameterHelper apiRequestParameterHelper,
	   final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,final PlanReadPlatformService planReadPlatformService) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.planReadPlatformService=planReadPlatformService;
		    }		
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String createPlan(final String apiRequestBodyAsJson) {

        final CommandWrapper commandRequest=new CommandWrapperBuilder().createPlan().withJson(apiRequestBodyAsJson).build();
		final CommandProcessingResult result=this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		  return this.toApiJsonSerializer.serialize(result);
	}

	@GET
	@Path("template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrievePlanTemplate(@Context final UriInfo uriInfo) {
		 context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
     List<ServiceData> data = this.planReadPlatformService.retrieveAllServices();
     List<BillRuleData> billData = this.planReadPlatformService.retrievebillRules();
	 List<SubscriptionData> contractPeriod = this.planReadPlatformService.retrieveSubscriptionData();
	 contractPeriod.remove(0);
	 List<EnumOptionData> status = this.planReadPlatformService.retrieveNewStatus();
	 List<SystemData> provisionSysData = this.planReadPlatformService.retrieveSystemData();
	 List<EnumOptionData> volumeType = this.planReadPlatformService.retrieveVolumeTypes();
	 PlanData planData = new PlanData(data, billData, contractPeriod, status,null,null,provisionSysData,volumeType);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, planData, RESPONSE_DATA_PARAMETERS);
	
	}
	
	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveAllPlans(@QueryParam("planType") final String planType,  @Context final UriInfo uriInfo) {
 		 context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		List<PlanData> products = this.planReadPlatformService.retrievePlanData(planType);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, products, RESPONSE_DATA_PARAMETERS);
	}
	@GET
	@Path("{planId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrievePlanDetails(@PathParam("planId") final Long planId,@Context final UriInfo uriInfo) {
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		PlanData singlePlandata = this.planReadPlatformService.retrievePlanData(planId);
		List<ServiceData> data = this.planReadPlatformService.retrieveAllServices();
		List<BillRuleData> billData = this.planReadPlatformService.retrievebillRules();
		List<SubscriptionData> contractPeriod = this.planReadPlatformService.retrieveSubscriptionData();
		contractPeriod.remove(0);
		List<EnumOptionData> status = this.planReadPlatformService.retrieveNewStatus();
		List<ServiceData> services = this.planReadPlatformService.retrieveSelectedServices(planId);
		List<SystemData> provisionSysData = this.planReadPlatformService.retrieveSystemData();
		 List<EnumOptionData> volumeType = this.planReadPlatformService.retrieveVolumeTypes();
		int size = data.size();
		int selectedsize = services.size();
			for (int i = 0; i < selectedsize; i++)
     			{
				Long selected = services.get(i).getId();
				for (int j = 0; j < size; j++) {
					Long avialble = data.get(j).getId();
					if (selected == avialble) {
						data.remove(j);
						size--;
					}
				}
			}
			// services.remove(data);
			singlePlandata = new PlanData(data, billData, contractPeriod,status, singlePlandata, services,provisionSysData,volumeType);
			final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
			return this.toApiJsonSerializer.serialize(settings, singlePlandata, RESPONSE_DATA_PARAMETERS);
	}
	@PUT
	@Path("{planCode}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String updatePlan(@PathParam("planCode") final Long planId,final String apiRequestBodyAsJson) {
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().updatePlan(planId).withJson(apiRequestBodyAsJson).build();
		 final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		  return this.toApiJsonSerializer.serialize(result);
	}
	
	
	 @DELETE
		@Path("{planCode}")
		@Consumes({MediaType.APPLICATION_JSON})
		@Produces({MediaType.APPLICATION_JSON})
		public String deletePlan(@PathParam("planCode") final Long planId) {
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().deletePlan(planId).build();
     final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
     return this.toApiJsonSerializer.serialize(result);

		}
	
	
}