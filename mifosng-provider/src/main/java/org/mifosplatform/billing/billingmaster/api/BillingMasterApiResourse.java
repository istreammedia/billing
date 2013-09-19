package org.mifosplatform.billing.billingmaster.api;

import java.io.File;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.billmaster.domain.BillMaster;
import org.mifosplatform.billing.billmaster.domain.BillMasterRepository;
import org.mifosplatform.billing.billmaster.service.BillMasterReadPlatformService;
import org.mifosplatform.billing.billmaster.service.BillMasterWritePlatformService;
import org.mifosplatform.billing.billmaster.service.BillWritePlatformService;
import org.mifosplatform.billing.financialtransaction.data.FinancialTransactionsData;
import org.mifosplatform.billing.order.service.OrderReadPlatformService;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;

@Path("/billmaster")
@Component
@Scope("singleton")
public class BillingMasterApiResourse {
	    private  final Set<String> RESPONSE_DATA_PARAMETERS=new HashSet<String>(Arrays.asList("transactionId","transactionDate","transactionType","amount",
			"invoiceId","chrageAmount","taxAmount","chargeType","amount","billDate","dueDate","id","transaction","chargeStartDate","chargeEndDate"));
        private final String resourceNameForPermissions = "BILLMASTER";
	    private final PlatformSecurityContext context;
	    private final DefaultToApiJsonSerializer<FinancialTransactionsData> toApiJsonSerializer;
	    private final ApiRequestParameterHelper apiRequestParameterHelper;
	    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	    private final BillMasterReadPlatformService billMasterReadPlatformService;
		private final OrderReadPlatformService orderReadPlatformService;
		private final BillMasterRepository billMasterRepository;
		private final BillWritePlatformService billWritePlatformService;
	    private final FromJsonHelper fromApiJsonHelper;
	    private final BillMasterWritePlatformService billMasterWritePlatformService;
		
		
		 @Autowired
	    public BillingMasterApiResourse(final PlatformSecurityContext context, final FromJsonHelper fromJsonHelper,
	    final DefaultToApiJsonSerializer<FinancialTransactionsData> toApiJsonSerializer, final ApiRequestParameterHelper apiRequestParameterHelper,
	    final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,final BillMasterReadPlatformService billMasterReadPlatformService,
	    OrderReadPlatformService orderReadPlatformService,final BillMasterRepository billMasterRepository,final BillWritePlatformService billWritePlatformService,
	    final BillMasterWritePlatformService billMasterWritePlatformService) {
		        this.context = context;
		        this.toApiJsonSerializer = toApiJsonSerializer;
		        this.apiRequestParameterHelper = apiRequestParameterHelper;
		        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		        this.billMasterReadPlatformService=billMasterReadPlatformService;
		        this.orderReadPlatformService=orderReadPlatformService;
		        this.billMasterRepository=billMasterRepository;
		        this.billWritePlatformService=billWritePlatformService;
		        this.fromApiJsonHelper=fromJsonHelper;
		        this.billMasterWritePlatformService=billMasterWritePlatformService;
		    }		
		

	@POST
	@Path("{clientId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveBillingProducts(@PathParam("clientId") final Long clientId,final String apiRequestBodyAsJson) {
		
		 final JsonElement parsedCommand = this.fromApiJsonHelper.parse(apiRequestBodyAsJson.toString());
         final JsonCommand command = JsonCommand.from(apiRequestBodyAsJson.toString(),parsedCommand,this.fromApiJsonHelper,
        		 "BILLMASTER",clientId,null,null,clientId,null,null,null,null,null,null);
		final CommandProcessingResult result=this.billMasterWritePlatformService.createBillMaster(command,command.entityId());
	  this.billWritePlatformService.ireportPdf(result.resourceId());
	    return this.toApiJsonSerializer.serialize(result);
	}
	@GET
	@Path("{clientId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveBillStatements(@PathParam("clientId") final Long clientId,@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final List<FinancialTransactionsData> data = this.billMasterReadPlatformService.retrieveStatments(clientId);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_DATA_PARAMETERS);
	}

	@GET
	@Path("{billId}/print")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response printInvoice(@PathParam("billId") final Long billId) {
		BillMaster billMaster = this.billMasterRepository.findOne(billId);
		String printFileName = billMaster.getFileName();
		File file = new File(printFileName);
		ResponseBuilder response = Response.ok(file);
		response.header("Content-Disposition", "attachment; filename=\""+ printFileName + "\"");
		response.header("Content-Type", "application/pdf");
		return response.build();
	}

}
