package org.mifosplatform.billing.chargecode.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.chargecode.data.BillFrequencyCodeData;
import org.mifosplatform.billing.chargecode.data.ChargeCodeData;
import org.mifosplatform.billing.chargecode.data.ChargeTypeData;
import org.mifosplatform.billing.chargecode.data.DurationTypeData;
import org.mifosplatform.billing.chargecode.service.ChargeCodeReadPlatformService;
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

@Path("/chargecode")
@Component
@Scope("singleton")
public class ChargeCodeApiResource {
	

	private final Set<String> RESPONSE_PARAMETERS = new HashSet<String>(Arrays.asList("id","chargeCode","chargeDescription","chargeType","chargeDurtion","durationType",
			"taxInclusive","billFrequencyCode"));
	private final String resourceNameForPermissions = "CHARGECODE";
	
	private PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService;
	private DefaultToApiJsonSerializer<ChargeCodeData> toApiJsonSerializer;
	private ApiRequestParameterHelper apiRequestParameterHelper;
	private PlatformSecurityContext context;
	private ChargeCodeReadPlatformService chargeCodeReadPlatformService;
	
	
	@Autowired
	public ChargeCodeApiResource(final PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService,
							     final DefaultToApiJsonSerializer<ChargeCodeData> toApiJsonSerializer,
							     final ApiRequestParameterHelper apiRequestParameterHelper,
							     final PlatformSecurityContext context,
							     final ChargeCodeReadPlatformService chargeCodeReadPlatformService) {
		this.commandSourceWritePlatformService = commandSourceWritePlatformService;
		this.toApiJsonSerializer = toApiJsonSerializer;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.context = context;
		this.chargeCodeReadPlatformService = chargeCodeReadPlatformService;
	}
	
	
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String getChargeCode(@Context final UriInfo uriInfo) {
		//context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		List<ChargeCodeData> chargeCode = this.chargeCodeReadPlatformService.getChargeCode();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, chargeCode, RESPONSE_PARAMETERS); 
		
	}
	
	@GET
	@Path("template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String getTemplateRelatedData(@Context final UriInfo uriInfo){
		//code here was copied from above Get method..
		//context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);		
		//List<ChargeCodeData> chargeCode = this.chargeCodeReadPlatformService.getChargeCode();
		List<ChargeTypeData> chargeTypeData = this.chargeCodeReadPlatformService.getChargeType();
		List<DurationTypeData> durationTypeData = this.chargeCodeReadPlatformService.getDurationType();
		List<BillFrequencyCodeData> billFrequencyData = this.chargeCodeReadPlatformService.getBillFrequency();
		
		ChargeCodeData chargeCodeData = new ChargeCodeData(null,chargeTypeData,durationTypeData,billFrequencyData);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, chargeCodeData, RESPONSE_PARAMETERS); 
	}
	
	@GET
	@Path("{chargeCodeId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String getChargeCodeForEdit(@PathParam("chargeCodeId") final Long chargeCodeId, @Context final UriInfo uriInfo){
		//context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		ChargeCodeData chargeCodeData = chargeCodeReadPlatformService.getChargeCode(chargeCodeId);
		chargeCodeData.setChargeTypeData(this.chargeCodeReadPlatformService.getChargeType());
		chargeCodeData.setDurationTypeData(this.chargeCodeReadPlatformService.getDurationType());
		chargeCodeData.setBillFrequencyCodeData(this.chargeCodeReadPlatformService.getBillFrequency());
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, chargeCodeData, RESPONSE_PARAMETERS); 
	}
	
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String addChargeCode(@Context final UriInfo uriInfo, String jsonRequestBody) {
		//context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final CommandWrapper commandRequest = new CommandWrapperBuilder().createChargeCode().withJson(jsonRequestBody).build();
		final CommandProcessingResult result  = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		
		return this.toApiJsonSerializer.serialize(result);
	}
	
	@PUT
	@Path("{chargeCodeIdForUpdate}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String updateChargeCode(@PathParam("chargeCodeIdForUpdate") final Long chargeCodeIdForUpdate, final String jsonRequestBody){
		//context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final CommandWrapper commandRequest = new CommandWrapperBuilder().updateChargeCode(chargeCodeIdForUpdate).withJson(jsonRequestBody).build();
		final CommandProcessingResult result  = this.commandSourceWritePlatformService.logCommandSource(commandRequest);
		return this.toApiJsonSerializer.serialize(result);
	}
	
}
