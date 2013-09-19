package org.mifosplatform.billing.inventory.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.inventory.data.InventoryGrnData;
import org.mifosplatform.billing.inventory.data.InventoryItemDetailsData;
import org.mifosplatform.billing.inventory.data.InventoryItemSerialNumberData;
import org.mifosplatform.billing.inventory.data.ItemMasterIdData;
import org.mifosplatform.billing.inventory.data.QuantityData;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetailsAllocation;
import org.mifosplatform.billing.inventory.exception.InventoryItemDetailsExist;
import org.mifosplatform.billing.inventory.service.InventoryGrnReadPlatformService;
import org.mifosplatform.billing.inventory.service.InventoryItemDetailsReadPlatformService;
import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.item.service.ItemReadPlatformService;
import org.mifosplatform.billing.supplier.data.SupplierData;
import org.mifosplatform.billing.supplier.service.SupplierReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.office.data.OfficeData;
import org.mifosplatform.organisation.office.service.OfficeReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/itemdetails")
@Component
@Scope("singleton")
public class InventoryItemDetailsApiResource {
	
	private final Set<String> RESPONSE_DATA_ITEM_DETAILS_PARAMETERS = new HashSet<String>(Arrays.asList("id", "itemMasterId", "serialNumber", "grnId","provisioningSerialNumber", "quality", "status","warranty", "remarks"));
	private final Set<String> RESPONSE_DATA_GRN_DETAILS_PARAMETERS = new HashSet<String>(Arrays.asList("id", "purchaseDate", "supplierId", "itemMasterId","orderdQuantity", "receivedQuantity"));
	private final Set<String> RESPONSE_DATA_SERIAL_NUMBER_PARAMETERS = new HashSet<String>(Arrays.asList("serialNumber"));
	private final Set<String> RESPONSE_DATA_GRN_IDS_PARAMETERS = new HashSet<String>(Arrays.asList("id"));
	
	private final String resourceNameForPermissionsGrn = "GRN";
    private final String resourceNameForPermissions = "INVENTORY";
    private final String resourceNameForPermissionsAllocation = "ALLOCATION";
	
	private final PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<InventoryItemDetailsData> toApiJsonSerializerForItem;
	private final DefaultToApiJsonSerializer<InventoryItemSerialNumberData> toApiJsonSerializerForItemSerialNumbers;
	private final DefaultToApiJsonSerializer<InventoryItemSerialNumberData> toApiJsonSerializerForAllocationHardware;
	private final DefaultToApiJsonSerializer<InventoryGrnData> toApiJsonSerializerForGrn;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private final InventoryGrnReadPlatformService inventoryGrnReadPlatformService;
	private final InventoryItemDetailsReadPlatformService itemDetailsReadPlatformService;
	private final DefaultToApiJsonSerializer<InventoryItemDetailsAllocation> toApiJsonSerializerForItemAllocation;
	private final OfficeReadPlatformService officeReadPlatformService;
	private final ItemReadPlatformService itemReadPlatformService;
	private final SupplierReadPlatformService supplierReadPlatformService;

	@Autowired
	public InventoryItemDetailsApiResource(final PlatformSecurityContext context,final DefaultToApiJsonSerializer<InventoryItemDetailsData> toApiJsonSerializerForItem,ApiRequestParameterHelper apiRequestParameterHelper,PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,final InventoryGrnReadPlatformService inventoryGrnReadPlatformService,final DefaultToApiJsonSerializer<InventoryGrnData> toApiJsonSerializerForGrn,InventoryItemDetailsReadPlatformService itemDetailsReadPlatformService, final DefaultToApiJsonSerializer<InventoryItemSerialNumberData> toApiJsonSerializerForItemSerialNumbers,final DefaultToApiJsonSerializer<InventoryItemDetailsAllocation> toApiJsonSerializerForItemAllocation,final DefaultToApiJsonSerializer<InventoryItemSerialNumberData> toApiJsonSerializerForAllocationHardware,
										   final OfficeReadPlatformService officeReadPlatformService,
										   final ItemReadPlatformService itemReadPlatformService,
										   SupplierReadPlatformService supplierReadPlatformService) {
		this.context=context;
	    this.toApiJsonSerializerForItem = toApiJsonSerializerForItem;
	    this.toApiJsonSerializerForGrn = toApiJsonSerializerForGrn;
	    this.apiRequestParameterHelper = apiRequestParameterHelper;
	    this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
	    this.inventoryGrnReadPlatformService = inventoryGrnReadPlatformService;
	    this.itemDetailsReadPlatformService = itemDetailsReadPlatformService;
	    this.toApiJsonSerializerForItemSerialNumbers = toApiJsonSerializerForItemSerialNumbers;
	    this.toApiJsonSerializerForItemAllocation = toApiJsonSerializerForItemAllocation;
	    this.toApiJsonSerializerForAllocationHardware = toApiJsonSerializerForAllocationHardware;
	    this.officeReadPlatformService = officeReadPlatformService;
	    this.itemReadPlatformService = itemReadPlatformService;
	    this.supplierReadPlatformService = supplierReadPlatformService;
	}

	/*
	 * for storing item details into b_item_detail table
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String addItemDetails(final String jsonRequestBody) {
		
		final CommandWrapper commandRequest = new CommandWrapperBuilder().createInventoryItem(null).withJson(jsonRequestBody).build();
		 final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	     return this.toApiJsonSerializerForItem.serialize(result);
	}
	
	/*
	 * this method is for storing GRN details into b_grn table
	 * */
	
	@POST
	@Path("addgrn")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String addGrnDetails(final String jsonRequestBody){
		
		final CommandWrapper commandRequest = new CommandWrapperBuilder().createGrn().withJson(jsonRequestBody).build();
		final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
		return this.toApiJsonSerializerForGrn.serialize(result);
	}

	@GET
	@Path("addgrn")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String addGrn(@Context final UriInfo uriInfo) {
		 context.authenticatedUser();
		 List<SupplierData> supplierData = this.supplierReadPlatformService.retrieveSupplier();
		 Collection<OfficeData> officeData = this.officeReadPlatformService.retrieveAllOfficesForDropdown();
		 List<ItemData> itemData = this.itemReadPlatformService.retrieveAllItems();
		 InventoryGrnData inventoryGrnData =  new InventoryGrnData(itemData,officeData,supplierData);
		 final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializerForGrn.serialize(settings, inventoryGrnData, RESPONSE_DATA_ITEM_DETAILS_PARAMETERS);
	}
	

	@POST
	@Path("allocation")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })/*@QueryParam("id") final Long id,*/
	public String allocateHardware(final String jsonRequestBody) {
		
		
		CommandWrapper command = new CommandWrapperBuilder().allocateHardware().withJson(jsonRequestBody).build();
		final CommandProcessingResult result = commandsSourceWritePlatformService.logCommandSource(command);
		return this.toApiJsonSerializerForItemAllocation.serialize(result);
	}
	
	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retriveItemDetails(@Context final UriInfo uriInfo) {
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final Collection<InventoryItemDetailsData> itemdetails = this.itemDetailsReadPlatformService.retriveAllItemDetails();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializerForItem.serialize(settings, itemdetails, RESPONSE_DATA_ITEM_DETAILS_PARAMETERS);
		

	}

	@GET
	@Path("template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String grnTemplate(@QueryParam("grnId") final Long grnId,@Context final UriInfo uriInfo) {
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		
		InventoryGrnData inventoryGrnData = null;
		boolean val = false;
		if (grnId != null)
			val = this.inventoryGrnReadPlatformService.validateForExist(grnId);
		if (val) {
			List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
			DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("Grn Details");
			baseDataValidator.reset().parameter("id").value(grnId).notBlank().notNull();
				throw new InventoryItemDetailsExist("Failure","validation.error.msg.grnid","","id");//throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist","Validation errors exist.", dataValidationErrors);
		}
		if (grnId == null) {
			inventoryGrnData = new InventoryGrnData();
			return this.toApiJsonSerializerForGrn.serialize(inventoryGrnData);

		}
		
		inventoryGrnData = this.inventoryGrnReadPlatformService.retriveGrnDetailTemplate(grnId);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializerForGrn.serialize(settings, inventoryGrnData, RESPONSE_DATA_GRN_DETAILS_PARAMETERS);
		
	}

	@GET
	@Path("{oneTimeSaleId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveItemSerialNumbers(@PathParam("oneTimeSaleId") final Long oneTimeSaleId, @Context final UriInfo uriInfo){
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissionsAllocation);
		
		
		List<QuantityData> itemSerialNumbers = this.itemDetailsReadPlatformService.retriveSerialNumbers(oneTimeSaleId);
		QuantityData quantityData = this.itemDetailsReadPlatformService.retriveQuantity(oneTimeSaleId);
		ItemMasterIdData itemMasterIdData = this.itemDetailsReadPlatformService.retriveItemMasterId(oneTimeSaleId);
		InventoryItemSerialNumberData data=new InventoryItemSerialNumberData(itemSerialNumbers, quantityData.getQuantity(),itemMasterIdData.getItemMasterId());
		  //List<InventoryItemSerialNumberData> datas=new ArrayList<InventoryItemSerialNumberData>();
		  
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializerForAllocationHardware.serialize(settings, data, RESPONSE_DATA_SERIAL_NUMBER_PARAMETERS);
		//return "SYED MUJEEB RAHMAN";
		
	}
	
	
	@GET
	@Path("grn/template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveGrnIds(@Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		Collection<InventoryGrnData> inventoryGrnData = this.inventoryGrnReadPlatformService.retriveGrnIds();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializerForGrn.serialize(settings,inventoryGrnData,RESPONSE_DATA_GRN_IDS_PARAMETERS);
	}
	
	
	/*
	 * this method is yet not implemented , who ever is implementing or using this code please remove this COMMENT.
	 * */
	@GET
	@Path("grn")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retriveGrnDetails(@Context final UriInfo uriInfo) {

		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		Collection<InventoryGrnData> inventoryGrnData = this.inventoryGrnReadPlatformService.retriveGrnDetails();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializerForGrn.serialize(settings, inventoryGrnData, RESPONSE_DATA_GRN_DETAILS_PARAMETERS);
	
	}

}
