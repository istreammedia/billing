package org.mifosplatform.billing.transactionhistory.api;

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

import org.mifosplatform.billing.transactionhistory.data.TransactionHistoryData;
import org.mifosplatform.billing.transactionhistory.service.TransactionHistoryReadPlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Path("transactionhistory")
@Component
@Scope("singleton")
public class TransactionHistoryApiResource {

	
	private String resourceType = "TRANSACTIONHISTORY";
	private PlatformSecurityContext context;
	private TransactionHistoryReadPlatformService transactionHistoryReadPlatformService;
	private ApiRequestParameterHelper apiRequestParameterHelper; 
	private DefaultToApiJsonSerializer<TransactionHistoryData> apiJsonSerializer;
	static final private Set<String> supportedResponseParameters = new HashSet<String>(Arrays.asList("id","clientId","user","transactionType","transactionDate","history"));
	
	@Autowired
	public TransactionHistoryApiResource(final PlatformSecurityContext context, final TransactionHistoryReadPlatformService transactionHistoryReadPlatformService, final ApiRequestParameterHelper apiRequestParameterHelper, final DefaultToApiJsonSerializer<TransactionHistoryData> apiJsonSerializer) {
		this.context = context;
		this.transactionHistoryReadPlatformService = transactionHistoryReadPlatformService;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.apiJsonSerializer = apiJsonSerializer;
	}
	
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String createTransactionHistory(@Context final UriInfo uriInfo, final String jsonRequestBody){
		
		return null;
	}
	
	@GET
	@Path("template")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String retriveTransactionHistoryTemplate(@Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		List<TransactionHistoryData> transactionHistory = transactionHistoryReadPlatformService.retriveTransactionHistoryTemplate();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.apiJsonSerializer.serialize(settings, transactionHistory, supportedResponseParameters);
		
	}
	
	@GET
	@Path("template/{clientId}")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public String retriveTransactionHistoryById(@PathParam("clientId") final Long clientId, @Context final UriInfo uriInfo){
		context.authenticatedUser().validateHasReadPermission(resourceType);
		List<TransactionHistoryData> transactionHistory = transactionHistoryReadPlatformService.retriveTransactionHistoryById(clientId);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return apiJsonSerializer.serialize(settings,transactionHistory,supportedResponseParameters);
	}
	
	
	
}
