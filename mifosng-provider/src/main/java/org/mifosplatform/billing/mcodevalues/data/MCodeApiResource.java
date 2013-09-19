package org.mifosplatform.billing.mcodevalues.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.mcodevalues.service.MCodeReadPlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Path("mcodes")
@Component
@Scope("singleton")
public class MCodeApiResource {

	private static final String resourceType = "MCODEVALUE";
	private static final Set<String> MCODE_RESPONSE_PARAMETER = new HashSet<String>(Arrays.asList("id","mCodeValue"));
	private PlatformSecurityContext context;
	private MCodeReadPlatformService mCodeReadPlatformService;
	private ApiRequestParameterHelper apiRequestParameterHelper;
	private DefaultToApiJsonSerializer<MCodeData> apiJsonSerializer;

	@Autowired
	public MCodeApiResource(final PlatformSecurityContext context, final MCodeReadPlatformService mCodeReadPlatformService, final ApiRequestParameterHelper apiRequestParameterHelper, final DefaultToApiJsonSerializer<MCodeData> apiJsonSerializer) {
		this.context = context;
		this.mCodeReadPlatformService = mCodeReadPlatformService;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.apiJsonSerializer = apiJsonSerializer;
	}
	
	
	@Path("{codeName}")
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retriveMCodes(@PathParam("codeName") final String codeName, @Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		Collection<MCodeData> mcodeData = mCodeReadPlatformService.getCodeValue(codeName);
		ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return apiJsonSerializer.serialize(settings,mcodeData,MCODE_RESPONSE_PARAMETER);
	}
	
	
}
