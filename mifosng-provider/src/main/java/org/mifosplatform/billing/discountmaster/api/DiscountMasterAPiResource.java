package org.mifosplatform.billing.discountmaster.api;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.billing.discountmaster.service.DiscountReadPlatformService;
import org.mifosplatform.billing.mcodevalues.data.MCodeData;
import org.mifosplatform.billing.mcodevalues.service.MCodeReadPlatformService;
import org.mifosplatform.billing.plan.data.BillRuleData;
import org.mifosplatform.billing.plan.data.PlanData;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.plan.data.SystemData;
import org.mifosplatform.billing.plan.service.PlanReadPlatformService;
import org.mifosplatform.billing.service.DiscountMasterData;
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

@Path("/discounts")
@Component
@Scope("singleton")
public class DiscountMasterAPiResource {
	
	
	private  final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("id", "discountCode","discountdescription","discountType",
			"discountRate","startDate","status","discountstatus"));
	 private final String resourceNameForPermissions = "DISCOUNT";
	  private final PlatformSecurityContext context;
	    private final DefaultToApiJsonSerializer<DiscountMasterData> toApiJsonSerializer;
	    private final ApiRequestParameterHelper apiRequestParameterHelper;
	    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	    private final DiscountReadPlatformService discountReadPlatformService;
	    private final PlanReadPlatformService planReadPlatformService;
	    private final MCodeReadPlatformService mCodeReadPlatformService;
	    
	    @Autowired
	    public DiscountMasterAPiResource(final PlatformSecurityContext context,final DefaultToApiJsonSerializer<DiscountMasterData> toApiJsonSerializer,
	    		final ApiRequestParameterHelper apiRequestParameterHelper,final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
	    		final DiscountReadPlatformService discountReadPlatformService,final PlanReadPlatformService planReadPlatformService,final MCodeReadPlatformService codeReadPlatformService) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.discountReadPlatformService=discountReadPlatformService;
		        this.planReadPlatformService=planReadPlatformService;
		        this.mCodeReadPlatformService=codeReadPlatformService;
		    }		
	    
	    @POST
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String createNewDiscount(final String apiRequestBodyAsJson) {

	        final CommandWrapper commandRequest=new CommandWrapperBuilder().createDiscount().withJson(apiRequestBodyAsJson).build();
			final CommandProcessingResult result=this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
			  return this.toApiJsonSerializer.serialize(result);
		}
	    
	    @GET
		@Path("template")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String retrieveDiscountTemplate(@Context final UriInfo uriInfo) {
			 
	    	context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
			 DiscountMasterData discountMasterData=handleTemplateData();
			final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
			return this.toApiJsonSerializer.serialize(settings, discountMasterData, RESPONSE_DATA_PARAMETERS);
		
		}
		
		private DiscountMasterData handleTemplateData() {
			 List<EnumOptionData> status = this.planReadPlatformService.retrieveNewStatus();
		     Collection<MCodeData> discountTypeData = mCodeReadPlatformService.getCodeValue("type");
		     return new DiscountMasterData(status,discountTypeData);
		}

		@GET
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String retrieveAllDiscountDetails(@QueryParam("planType") final String planType,  @Context final UriInfo uriInfo) {
	 		 context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
			List<DiscountMasterData> discountMasterDatas= this.discountReadPlatformService.retrieveAllDiscounts();
			final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
			return this.toApiJsonSerializer.serialize(settings,discountMasterDatas, RESPONSE_DATA_PARAMETERS);
		}
	    
		
		@GET
		@Path("{discountId}")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String retrieveSingleDiscountDetails(@PathParam("discountId") final Long discountId,@Context final UriInfo uriInfo) {
			
			context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
			DiscountMasterData discountMasterData= this.discountReadPlatformService.retrieveDiscountDetails(discountId);
			List<EnumOptionData> status = this.planReadPlatformService.retrieveNewStatus();
		     Collection<MCodeData> discountTypeData = mCodeReadPlatformService.getCodeValue("type");
		     discountMasterData.setStatus(status);
		     discountMasterData.setDiscounTypeData(discountTypeData);
		     final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
			 return this.toApiJsonSerializer.serialize(settings, discountMasterData, RESPONSE_DATA_PARAMETERS);
		}
		
		@PUT
		@Path("{discountId}")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String updateDiscount(@PathParam("discountId") final Long discountId,final String apiRequestBodyAsJson) {
			 final CommandWrapper commandRequest = new CommandWrapperBuilder().updateDiscount(discountId).withJson(apiRequestBodyAsJson).build();
			 final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
			  return this.toApiJsonSerializer.serialize(result);
		}
		
		
		 @DELETE
			@Path("{discountId}")
			@Consumes({MediaType.APPLICATION_JSON})
			@Produces({MediaType.APPLICATION_JSON})
			public String deletePlan(@PathParam("discountId") final Long discountId) {
			 final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteDiscount(discountId).build();
	     final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	     return this.toApiJsonSerializer.serialize(result);

			}

}
