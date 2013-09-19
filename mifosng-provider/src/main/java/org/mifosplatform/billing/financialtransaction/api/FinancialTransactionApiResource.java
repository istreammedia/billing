package org.mifosplatform.billing.financialtransaction.api;

import java.util.Arrays;
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

import org.mifosplatform.billing.billmaster.service.BillMasterReadPlatformService;
import org.mifosplatform.billing.financialtransaction.data.FinancialTransactionsData;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;




@Path("/financialTransactions")
@Component
@Scope("singleton")
public class FinancialTransactionApiResource {
	private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("transactionId","transactionDate","transactionType","amount",
			"invoiceId","chrageAmount","taxAmount","chargeType","amount","billDate","dueDate","id","transaction","chargeStartDate","chargeEndDate"));
	private BillMasterReadPlatformService billMasterReadPlatformService;
	private PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<FinancialTransactionsData> toApiJsonSerializer;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final String resourceNameForPermissions = "financialTransactions";
    @Autowired
    public FinancialTransactionApiResource(final BillMasterReadPlatformService billMasterReadPlatformService,final PlatformSecurityContext context,
    		final DefaultToApiJsonSerializer<FinancialTransactionsData> toApiJsonSerializer,final ApiRequestParameterHelper apiRequestParameterHelper,
    		final PortfolioCommandSourceWritePlatformService portfolioCommandSourceWritePlatformService){
    	this.apiRequestParameterHelper=apiRequestParameterHelper;
    	this.billMasterReadPlatformService=billMasterReadPlatformService;
    	this.commandsSourceWritePlatformService=portfolioCommandSourceWritePlatformService;
    	this.context=context;
    	this.toApiJsonSerializer=toApiJsonSerializer;
    
    	
    }
	@GET
	@Path("{clientId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retrieveTransactionalData(@PathParam("clientId") final Long clientId,@Context final UriInfo uriInfo) {
	context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
	List<FinancialTransactionsData> transactionData = this.billMasterReadPlatformService.retrieveInvoiceFinancialData(clientId);
	final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	return this.toApiJsonSerializer.serialize(settings, transactionData, RESPONSE_DATA_PARAMETERS);    
	}
	
	@GET
	@Path("{invoiceId}/invoice")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String retrieveInvoiceData(@PathParam("invoiceId") final Long invoiceId,@Context final UriInfo uriInfo) {
	context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
	List<FinancialTransactionsData> transactionData = this.billMasterReadPlatformService.retrieveSingleInvoiceData(invoiceId);
	FinancialTransactionsData data=new FinancialTransactionsData(transactionData);
    final ApiRequestJsonSerializationSettings settings=apiRequestParameterHelper.process(uriInfo.getQueryParameters());
    return this.toApiJsonSerializer.serialize(settings,data,RESPONSE_DATA_PARAMETERS);
	}

}
