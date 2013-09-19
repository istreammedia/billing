package org.mifosplatform.billing.currency.api;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.address.service.AddressReadPlatformService;
import org.mifosplatform.billing.currency.data.CountryCurrencyData;
import org.mifosplatform.billing.currency.service.CountryCurrencyReadPlatformService;
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
import org.mifosplatform.organisation.monetary.data.ApplicationCurrencyConfigurationData;
import org.mifosplatform.organisation.monetary.service.OrganisationCurrencyReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Path("/countrycurrencys")
@Component
@Scope("singleton")
public class CountryCurrecnyApiResource {
	
	private final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("id","country","currency","status"));
	
	private final String resorceNameForPermission="COUNTRYCURRENCY";
	private final PlatformSecurityContext context;
    private final DefaultToApiJsonSerializer<CountryCurrencyData> toApiJsonSerializer;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final CountryCurrencyReadPlatformService countryCurrencyReadPlatformService;
    private final OrganisationCurrencyReadPlatformService currencyReadPlatformService;
    private final AddressReadPlatformService addressReadPlatformService;
    private final PlanReadPlatformService planReadPlatformService;
    
    
@Autowired    
public  CountryCurrecnyApiResource(final PlatformSecurityContext context,final DefaultToApiJsonSerializer<CountryCurrencyData> toApiJsonSerializer,
    		final ApiRequestParameterHelper apiRequestParameterHelper,final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
    		CountryCurrencyReadPlatformService countryCurrencyReadPlatformService,final OrganisationCurrencyReadPlatformService currencyReadPlatformService,
    		final AddressReadPlatformService addressReadPlatformService,final PlanReadPlatformService planReadPlatformService) {
    			        this.context = context;
    			        this.toApiJsonSerializer = toApiJsonSerializer;
    			        this.apiRequestParameterHelper = apiRequestParameterHelper;
    			        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    			        this.countryCurrencyReadPlatformService=countryCurrencyReadPlatformService;
    			        this.currencyReadPlatformService=currencyReadPlatformService;
    			        this.addressReadPlatformService=addressReadPlatformService;
    			        this.planReadPlatformService=planReadPlatformService;
    			        
    }
    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({MediaType.APPLICATION_JSON })
    public String createCountryCurrency(final String apiRequestBodyAsJson){
    	final CommandWrapper commandRequest = new CommandWrapperBuilder().createCountryCurrency().withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);
	
}
    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String getCurrencuConfigDetails(@Context final UriInfo uriInfo) {
	   context.authenticatedUser().validateHasReadPermission(resorceNameForPermission);
		final Collection<CountryCurrencyData> currencyDatas = this.countryCurrencyReadPlatformService.retrieveCurrencyConfigurationDetails();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, currencyDatas, RESPONSE_DATA_PARAMETERS);
	}
    
    @GET
    @Path("template")
    @Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String getCurrencuConfigTemplateDetails(@Context final UriInfo uriInfo) {
	   context.authenticatedUser().validateHasReadPermission(resorceNameForPermission);
	   final ApplicationCurrencyConfigurationData configurationData = this.currencyReadPlatformService.retrieveCurrencyConfiguration();
	   List<String> countryData = this.addressReadPlatformService.retrieveCountryDetails();
	   List<EnumOptionData> status = this.planReadPlatformService.retrieveNewStatus();
	   CountryCurrencyData currencyData=new CountryCurrencyData(null,configurationData,countryData,status);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, currencyData, RESPONSE_DATA_PARAMETERS);
	}
    
    @GET
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String getSingleCurrencuConfigDetails(@PathParam("id") final Long id,@Context final UriInfo uriInfo) {
	   context.authenticatedUser().validateHasReadPermission(resorceNameForPermission);
	   final CountryCurrencyData currencyDatas = this.countryCurrencyReadPlatformService.retrieveCurrencyConfigurationDetails(id);
	   final ApplicationCurrencyConfigurationData configurationData = this.currencyReadPlatformService.retrieveCurrencyConfiguration();
	   List<String> countryData = this.addressReadPlatformService.retrieveCountryDetails();
	   List<EnumOptionData> status = this.planReadPlatformService.retrieveNewStatus();
	   CountryCurrencyData currencyData=new CountryCurrencyData(currencyDatas,configurationData,countryData,status);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, currencyData, RESPONSE_DATA_PARAMETERS);
	}
    
    @PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String updateService(@PathParam("id") final Long id, final String apiRequestBodyAsJson){

		//final ServiceMasterCommand command = this.apiDataConversionService.convertJsonToServiceMasterCommand(null, jsonRequestBody);
	 final CommandWrapper commandRequest = new CommandWrapperBuilder().updateCountryCurrency(id).withJson(apiRequestBodyAsJson).build();
	 final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	  return this.toApiJsonSerializer.serialize(result);
	}
	 @DELETE
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String deleteSubscription(@PathParam("id") final Long id) {
	 final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteCuntryCurrency(id).build();
    final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
    return this.toApiJsonSerializer.serialize(result);

	}

}
