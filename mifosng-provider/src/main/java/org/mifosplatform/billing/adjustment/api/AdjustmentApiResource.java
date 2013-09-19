package org.mifosplatform.billing.adjustment.api;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.adjustment.data.AdjustmentCodeData;
import org.mifosplatform.billing.adjustment.data.AdjustmentData;
import org.mifosplatform.billing.adjustment.domain.Discount;
import org.mifosplatform.billing.adjustment.service.AdjustmentReadPlatformService;
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

@Path("/adjustments")
@Component
@Scope("singleton")
public class AdjustmentApiResource {
	
	 private final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("discountCode", "discountOptions"));
	    private final String resourceNameForPermissions = "ADJUSTMENT";
	private final PlatformSecurityContext context;
    private final AdjustmentReadPlatformService readPlatformService;
    private final DefaultToApiJsonSerializer<AdjustmentCodeData> toApiJsonSerializer;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    

    @Autowired
    public AdjustmentApiResource(final PlatformSecurityContext context, final AdjustmentReadPlatformService readPlatformService,
            final DefaultToApiJsonSerializer<AdjustmentCodeData> toApiJsonSerializer, final ApiRequestParameterHelper apiRequestParameterHelper,
            final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
        this.context = context;
        this.readPlatformService = readPlatformService;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    }
    
    @POST
    @Path("{clientId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String addNewAdjustment(@PathParam("clientId") final Long clientId, final String apiRequestBodyAsJson) {
    	
    	final CommandWrapper commandRequest = new CommandWrapperBuilder().createAdjustment(clientId).withJson(apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.toApiJsonSerializer.serialize(result);
    }
    
    
    @GET
    @Path("template")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String retrieveTempleteInfo(@Context final UriInfo uriInfo) {
    	context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
    	final Collection<Discount> datass=new ArrayList<Discount>();
        Discount d=new Discount();
        d.setadjustment_type();
        datass.add(d);
        List<AdjustmentData> data=this.readPlatformService.retrieveAllAdjustmentsCodes();
        AdjustmentCodeData datas=new AdjustmentCodeData(datass,data);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, datas, RESPONSE_DATA_PARAMETERS);
    }
    

}
