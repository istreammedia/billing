package org.mifosplatform.billing.randomgenerator.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.randomgenerator.data.RandomGeneratorData;
import org.mifosplatform.billing.randomgenerator.service.RandomGeneratorReadPlatformService;
import org.mifosplatform.billing.randomgenerator.service.RandomGeneratorWritePlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.codes.data.CodeData;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/randomgenerators")
@Component
@Scope("singleton")
public class RandomGeneratorApiReswource {

		/**
		 * The set of parameters that are supported in response for {@link CodeData}
		 */
		private static final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("id", "batchName", "batchDescription", "length",
				"beginWith", "pinCategory", "pinType", "quantity","serialNo","expiryDate","dateFormat","pinValue","pinNO","locale","pinExtention"));
		private final String resourceNameForPermissions = "GETPAYMENT";

		private final PlatformSecurityContext context;
		private final RandomGeneratorReadPlatformService readPlatformService;
		private final DefaultToApiJsonSerializer<RandomGeneratorData> toApiJsonSerializer;
		private final ApiRequestParameterHelper apiRequestParameterHelper;
		private final RandomGeneratorWritePlatformService randomGeneratorWritePlatformService;
		private final PortfolioCommandSourceWritePlatformService writePlatformService;
	   
		@Autowired
		public RandomGeneratorApiReswource(final PlatformSecurityContext context,final RandomGeneratorReadPlatformService readPlatformService,
				final DefaultToApiJsonSerializer<RandomGeneratorData> toApiJsonSerializer,final ApiRequestParameterHelper apiRequestParameterHelper,
				final PortfolioCommandSourceWritePlatformService writePlatformService,
				RandomGeneratorWritePlatformService randomGeneratorWritePlatformService) {
			this.context = context;
			this.readPlatformService = readPlatformService;
			this.toApiJsonSerializer = toApiJsonSerializer;
			this.apiRequestParameterHelper = apiRequestParameterHelper;
			this.randomGeneratorWritePlatformService=randomGeneratorWritePlatformService;
			this.writePlatformService = writePlatformService;
			   
		}

		@POST
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String createPayment(final String apiRequestBodyAsJson) {
			final CommandWrapper commandRequest = new CommandWrapperBuilder().createRandomGenerator().withJson(apiRequestBodyAsJson).build();
			final CommandProcessingResult result = this.writePlatformService.logCommandSource(commandRequest);
			return this.toApiJsonSerializer.serialize(result);
		}

		@GET
		@Path("template")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String retrieveDetailsForPayments(@QueryParam("clientId") final Long clientId,@Context final UriInfo uriInfo) {
			context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
			 List<EnumOptionData> pinCategoryData =this.readPlatformService.pinCategory();
			 List<EnumOptionData> pinTypeData =this.readPlatformService.pinType();
			 RandomGeneratorData randomGeneratorData=new RandomGeneratorData(pinCategoryData,pinTypeData);
			final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
			return this.toApiJsonSerializer.serialize(settings, randomGeneratorData,RESPONSE_DATA_PARAMETERS);

		}
		
		@GET
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String retrieveDetails(@Context final UriInfo uriInfo) {
			context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
			List<RandomGeneratorData> randomGenerator=this.readPlatformService.getAllData();
			final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
			return this.toApiJsonSerializer.serialize(settings, randomGenerator,RESPONSE_DATA_PARAMETERS);

		}
	
}
