package org.mifosplatform.billing.onetimesale.api;

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

import org.mifosplatform.billing.charge.data.ChargesData;
import org.mifosplatform.billing.eventorder.data.EventOrderData;
import org.mifosplatform.billing.eventorder.service.EventOrderReadplatformServie;
import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.item.service.ItemReadPlatformService;
import org.mifosplatform.billing.onetimesale.data.AllocationDetailsData;
import org.mifosplatform.billing.onetimesale.data.OneTimeSaleData;
import org.mifosplatform.billing.onetimesale.service.OneTimeSaleReadPlatformService;
import org.mifosplatform.billing.onetimesale.service.OneTimeSaleWritePlatformService;
import org.mifosplatform.billing.pricing.service.PriceReadPlatformService;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.api.JsonQuery;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;



@Path("/onetimesales")
@Component
@Scope("singleton")
public class OneTimeSalesApiResource {
	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("itemId","chargedatas","itemDatas",
            "units","unitPrice","saleDate","totalprice","quantity","flag","allocationData","discountMasterDatas","id","eventName","bookedDate",
            "eventPrice","chargeCode","status"));
    private final String resourceNameForPermissions = "ONETIMESALE";
    private final PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<OneTimeSaleData> toApiJsonSerializer;
	private final DefaultToApiJsonSerializer<ItemData> defaultToApiJsonSerializer;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private final OneTimeSaleWritePlatformService oneTimeSaleWritePlatformService;
	private final OneTimeSaleReadPlatformService oneTimeSaleReadPlatformService;
	private final  ItemReadPlatformService itemMasterReadPlatformService;
	private final  PriceReadPlatformService priceReadPlatformService;
	private final EventOrderReadplatformServie eventOrderReadplatformServie;
	private final FromJsonHelper fromJsonHelper;
		 @Autowired
	    public OneTimeSalesApiResource(final PlatformSecurityContext context,final DefaultToApiJsonSerializer<OneTimeSaleData> toApiJsonSerializer,
	    final ApiRequestParameterHelper apiRequestParameterHelper,final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
	    final OneTimeSaleWritePlatformService oneTimeSaleWritePlatformService,final OneTimeSaleReadPlatformService oneTimeSaleReadPlatformService,
	    final ItemReadPlatformService itemReadPlatformService,final PriceReadPlatformService priceReadPlatformService,final EventOrderReadplatformServie eventOrderReadplatformServie,
	    final DefaultToApiJsonSerializer<ItemData> defaultToApiJsonSerializer,final FromJsonHelper fromJsonHelper) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
                this.oneTimeSaleWritePlatformService=oneTimeSaleWritePlatformService;
                this.oneTimeSaleReadPlatformService=oneTimeSaleReadPlatformService;
                this.itemMasterReadPlatformService=itemReadPlatformService;
                this.priceReadPlatformService=priceReadPlatformService;
                this.defaultToApiJsonSerializer=defaultToApiJsonSerializer;
                this.fromJsonHelper=fromJsonHelper;
                this.eventOrderReadplatformServie=eventOrderReadplatformServie;
		 }		
		
	

	@POST
	@Path("{clientId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String createNewSale(@PathParam("clientId") final Long clientId,final String apiRequestBodyAsJson) {
	 final CommandWrapper commandRequest = new CommandWrapperBuilder().createOneTimeSale(clientId).withJson(apiRequestBodyAsJson).build();
     final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
     return this.toApiJsonSerializer.serialize(result);
	}

	@GET
	@Path("template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveItemTemplateData(@Context final UriInfo uriInfo) {
		 context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		 
		OneTimeSaleData data=null;
		 data= handleTemplateRelatedData(data);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
	}

	private OneTimeSaleData handleTemplateRelatedData(OneTimeSaleData salesData) {
		List<ChargesData> chargeDatas = this.priceReadPlatformService.retrieveChargeCode();
		List<ItemData> itemData = this.oneTimeSaleReadPlatformService.retrieveItemData();
		List<DiscountMasterData> discountdata = this.priceReadPlatformService.retrieveDiscountDetails();
		return new OneTimeSaleData(chargeDatas,itemData,salesData,discountdata);
	}
	
	@GET
	@Path("{clientId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveClientOneTimeSaleDetails(@PathParam("clientId") final Long clientId,@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		List<OneTimeSaleData> salesData = this.oneTimeSaleReadPlatformService.retrieveClientOneTimeSalesData(clientId);
		List<EventOrderData> eventOrderDatas=this.eventOrderReadplatformServie.getTheClientEventOrders(clientId);
		OneTimeSaleData data=new OneTimeSaleData(salesData,eventOrderDatas);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
	}
	
	
	@GET
	 @Path("{itemId}/item")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveSingleItemDetails(@PathParam("itemId") final Long itemId,@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		List<ItemData> itemCodeData = this.oneTimeSaleReadPlatformService.retrieveItemData();
		List<DiscountMasterData> discountdata = this.priceReadPlatformService.retrieveDiscountDetails();
	    ItemData  itemData = this.itemMasterReadPlatformService.retrieveSingleItemDetails(itemId);
		itemData=new ItemData(itemCodeData,itemData,null,null,discountdata);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.defaultToApiJsonSerializer.serialize(settings, itemData, RESPONSE_DATA_PARAMETERS);		
	}
	
	
	@POST
	@Path("{itemId}/totalprice")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveTotalPrice(@PathParam("itemId") final Long itemId,@Context final UriInfo uriInfo,final String apiRequestBodyAsJson) {

		final JsonElement parsedQuery = this.fromJsonHelper.parse(apiRequestBodyAsJson);
         final JsonQuery query = JsonQuery.from(apiRequestBodyAsJson, parsedQuery, this.fromJsonHelper);
         ItemData itemData=oneTimeSaleWritePlatformService.calculatePrice(itemId,query);
         final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
 		return this.defaultToApiJsonSerializer.serialize(settings, itemData, RESPONSE_DATA_PARAMETERS);	
	}

	@GET
	@Path("{saleId}/oneTimeSale")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveSingleOneTimeSaleData(@PathParam("saleId") final Long saleId,@Context final UriInfo uriInfo) {
		 context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		OneTimeSaleData salesData = this.oneTimeSaleReadPlatformService.retrieveSingleOneTimeSaleDetails(saleId);
		salesData = handleTemplateRelatedData(salesData);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, salesData, RESPONSE_DATA_PARAMETERS);	
	}
	

	@GET
	@Path("{orderId}/allocation")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveItemAllocationDetails(@PathParam("orderId") final Long orderId,@Context final UriInfo uriInfo) {
		 context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		List<AllocationDetailsData> Data = this.oneTimeSaleReadPlatformService.retrieveAllocationDetails(orderId);
		OneTimeSaleData salesData=new OneTimeSaleData();
		salesData.setAllocationDetails(Data);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, salesData, RESPONSE_DATA_PARAMETERS);	
	}
		
	}


