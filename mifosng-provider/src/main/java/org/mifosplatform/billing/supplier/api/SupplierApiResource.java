package org.mifosplatform.billing.supplier.api;

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

import org.mifosplatform.billing.inventory.mrn.data.MRNDetailsData;
import org.mifosplatform.billing.supplier.data.SupplierData;
import org.mifosplatform.billing.supplier.service.SupplierReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.ToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Path("/suppliers")
@Component
@Scope("singleton")
public class SupplierApiResource {
	
	private final Set<String> RESPONSE_PARAMETERS = new HashSet<String>(Arrays.asList("id","supplierCode","supplierDescription","supplierAddress"));
	private final static String resourceType = "SUPPLIER";
	
	final private PlatformSecurityContext context;
	final private PortfolioCommandSourceWritePlatformService portfolioCommandSourceWritePlatformService;
	final private ApiRequestParameterHelper apiRequestParameterHelper;
	final private ToApiJsonSerializer<SupplierData> apiJsonSerializer;
	final private SupplierReadPlatformService supplierReadPlatformService;
	
	@Autowired
	public SupplierApiResource(final PlatformSecurityContext context, final PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService
			, final ApiRequestParameterHelper apiRequestParameterHelper
			, final ToApiJsonSerializer<SupplierData> apiJsonSerializer
			, final SupplierReadPlatformService supplierReadPlatformService) {
		
		this.context = context;
		this.portfolioCommandSourceWritePlatformService = commandSourceWritePlatformService;
		this.apiJsonSerializer = apiJsonSerializer;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.supplierReadPlatformService = supplierReadPlatformService;
		
	}
	
	
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveSupplierDetails(@Context final UriInfo uriInfo ){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		final List<SupplierData> mrnDetailsDatas = supplierReadPlatformService.retrieveSupplier();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return apiJsonSerializer.serialize(settings,mrnDetailsDatas,RESPONSE_PARAMETERS);
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String createMRN(final String jsonRequestBody){
		final CommandWrapper command = new CommandWrapperBuilder().createSupplier().withJson(jsonRequestBody).build();
		final CommandProcessingResult result = portfolioCommandSourceWritePlatformService.logCommandSource(command);
		return apiJsonSerializer.serialize(result);
	}
	
	
}
