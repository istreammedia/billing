package org.mifosplatform.billing.price.api;

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

import org.mifosplatform.billing.charge.data.ChargesData;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.priceregion.data.PriceRegionData;
import org.mifosplatform.billing.priceregion.service.RegionalPriceReadplatformService;
import org.mifosplatform.billing.pricing.data.PricingData;
import org.mifosplatform.billing.pricing.service.PriceReadPlatformService;
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

@Path("/prices")
@Component
@Scope("singleton")
public class PricingApiResource {

	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("planCode","planId","serviceId","chargeId","price",
			"chargeVariantId","discountId","planCode","id", "serviceData","chargeData","data", "chargeCode","chargeVaraint","price","priceregion","priceRegionData"));
    private final String resourceNameForPermissions = "PRICE";
	  private final PlatformSecurityContext context;
	    private final DefaultToApiJsonSerializer<PricingData> toApiJsonSerializer;
	    private final ApiRequestParameterHelper apiRequestParameterHelper;
	    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	    private final PriceReadPlatformService priceReadPlatformService;
	    private final RegionalPriceReadplatformService regionalPriceReadplatformService;
	    @Autowired
	    public PricingApiResource(final PlatformSecurityContext context,final RegionalPriceReadplatformService regionalPriceReadplatformService, 
	   final DefaultToApiJsonSerializer<PricingData> toApiJsonSerializer, final ApiRequestParameterHelper apiRequestParameterHelper,
	   final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,final PriceReadPlatformService priceReadPlatformService) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.priceReadPlatformService=priceReadPlatformService;
		        this.regionalPriceReadplatformService=regionalPriceReadplatformService;
		    }	
	@POST
	@Path("{planId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String createPrice(@PathParam("planId") final Long planId,final String apiRequestBodyAsJson) {
		 final CommandWrapper commandRequest = new CommandWrapperBuilder().createPrice(planId).withJson(apiRequestBodyAsJson).build();
	        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	        return this.toApiJsonSerializer.serialize(result);
		
	}
	@GET
	@Path("template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrievePricing(@QueryParam("planId") final Long planCode,@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		List<ServiceData> serviceData = this.priceReadPlatformService.retrievePrcingDetails(planCode);
		List<ChargesData> chargeDatas = this.priceReadPlatformService.retrieveChargeCode();
		List<EnumOptionData> datas = this.priceReadPlatformService.retrieveChargeVariantData();
		List<DiscountMasterData> discountdata = this.priceReadPlatformService.retrieveDiscountDetails();
		List<PriceRegionData> priceRegionData = this.regionalPriceReadplatformService.getThePriceregionsDetails();
		PricingData data = new PricingData(serviceData, chargeDatas, datas,	discountdata, serviceData.get(0).getPlanCode(), planCode,null,priceRegionData);
		 final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	     return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
	}

	@GET
	@Path("{planCode}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrievePrice(@PathParam("planCode") final String planCode,@Context final UriInfo uriInfo) {
	 context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
	List<ServiceData> serviceData = this.priceReadPlatformService.retrievePriceDetails(planCode);
	PricingData data = new PricingData(serviceData);
	final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
    return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
	}
	@GET
	@Path("{priceId}/update")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveIndividualPrice(@PathParam("priceId") final String priceId,@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		PricingData singlePriceData = this.priceReadPlatformService.retrieveSinglePriceDetails(priceId);
		List<ServiceData> serviceData = this.priceReadPlatformService.retrievePrcingDetails(singlePriceData.getPlanId());
		List<ChargesData> chargeDatas = this.priceReadPlatformService.retrieveChargeCode();
		List<EnumOptionData> datas = this.priceReadPlatformService.retrieveChargeVariantData();
		List<DiscountMasterData> discountdata = this.priceReadPlatformService.retrieveDiscountDetails();
		List<PriceRegionData> priceRegionData = this.regionalPriceReadplatformService.getThePriceregionsDetails();
		singlePriceData = new PricingData(serviceData, chargeDatas, datas,discountdata, serviceData.get(0).getCode(),
		singlePriceData.getPlanId(),singlePriceData,priceRegionData);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	    return this.toApiJsonSerializer.serialize(settings, singlePriceData, RESPONSE_DATA_PARAMETERS);
	}
	 @PUT
	 @Path("{priceId}/update")
		@Consumes({MediaType.APPLICATION_JSON})
		@Produces({MediaType.APPLICATION_JSON})
		public String updatePrice(@PathParam("priceId") final Long priceId, final String apiRequestBodyAsJson){
    	final CommandWrapper commandRequest = new CommandWrapperBuilder().updatePrice(priceId).withJson(apiRequestBodyAsJson).build();
		final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		return this.toApiJsonSerializer.serialize(result);
		}
	 @DELETE
		@Path("{priceId}")
		@Consumes({MediaType.APPLICATION_JSON})
		@Produces({MediaType.APPLICATION_JSON})
		public String deletePrice(@PathParam("priceId") final Long priceId) {
		final CommandWrapper commandRequest = new CommandWrapperBuilder().deletePrice(priceId).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);

		}
}
	