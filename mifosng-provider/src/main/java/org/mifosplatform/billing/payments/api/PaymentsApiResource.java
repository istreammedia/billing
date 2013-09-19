package org.mifosplatform.billing.payments.api;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.payments.data.PaymentData;
import org.mifosplatform.billing.paymode.data.McodeData;
import org.mifosplatform.billing.paymode.service.PaymodeReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.codes.data.CodeData;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

@Path("/payments")
@Component
@Scope("singleton")
public class PaymentsApiResource {

	/**
	 * The set of parameters that are supported in response for {@link CodeData}
	 */
	private static final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<String>(
			Arrays.asList("id", "clientId", "paymentDate", "paymentCode",
					"amountPaid", "statmentId", "externalId", "Remarks"));
	private final String resourceNameForPermissions = "GETPAYMENT";

	private final PlatformSecurityContext context;
	private final PaymodeReadPlatformService readPlatformService;
	private final DefaultToApiJsonSerializer<PaymentData> toApiJsonSerializer;
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PortfolioCommandSourceWritePlatformService writePlatformService;

	@Autowired
	public PaymentsApiResource(final PlatformSecurityContext context,final PaymodeReadPlatformService readPlatformService,
			final DefaultToApiJsonSerializer<PaymentData> toApiJsonSerializer,final ApiRequestParameterHelper apiRequestParameterHelper,
			final PortfolioCommandSourceWritePlatformService writePlatformService) {
		this.context = context;
		this.readPlatformService = readPlatformService;
		this.toApiJsonSerializer = toApiJsonSerializer;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.writePlatformService = writePlatformService;
	}

	@POST
	@Path("{clientId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String createPayment(@PathParam("clientId") final Long clientId,	final String apiRequestBodyAsJson) {
		final CommandWrapper commandRequest = new CommandWrapperBuilder().createPayment(clientId).withJson(apiRequestBodyAsJson).build();
		final CommandProcessingResult result = this.writePlatformService.logCommandSource(commandRequest);
		return this.toApiJsonSerializer.serialize(result);
	}

	@GET
	@Path("template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveDetailsForPayments(@QueryParam("clientId") final Long clientId,@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		Collection<McodeData> data = this.readPlatformService.retrievemCodeDetails("Payment Mode");
		PaymentData paymentData=new PaymentData(data);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, paymentData,RESPONSE_DATA_PARAMETERS);

	}
	
	@GET
	@Path("{clientId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrieveAllDetailsForPayments(@PathParam("clientId") final Long clientId,@Context final UriInfo uriInfo) {
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		List<PaymentData> paymentData = readPlatformService.retrivePaymentsData(clientId);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.toApiJsonSerializer.serialize(settings, paymentData,RESPONSE_DATA_PARAMETERS);

	}
	
	
	@POST
	 @Path("paypal")
	 @Consumes("application/x-www-form-urlencoded")
	 //@Consumes({ MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON ,MediaType.APPLICATION_OCTET_STREAM,MediaType.TEXT_PLAIN,MediaType.WILDCARD })
	    @Produces({ MediaType.APPLICATION_JSON })
	 public String Checkout(@FormParam("txn_id") String txnId,@FormParam("payment_date") Date Date,@FormParam("mc_gross") BigDecimal amount,@FormParam("address_name") String name,@FormParam("payer_email") String payerEmail){
	   try {
	     // ResourceBundle bundle = ResourceBundle.getBundle("usercredentials",Locale.getDefault());
	      
	     
	     
	
	  SimpleDateFormat daformat=new SimpleDateFormat("dd MMMM yyyy");
	  String date=daformat.format(Date);
	  JsonObject object=new JsonObject();
	  object.addProperty("txn_id", txnId);
	  object.addProperty("dateFormat","dd MMMM yyyy");
	  object.addProperty("locale","en");
	  object.addProperty("paymentDate",date);
	  object.addProperty("amountPaid",amount);
	  object.addProperty("isChequeSelected","no");
	  object.addProperty("receiptNo","1234");
	  object.addProperty("paypalemail", payerEmail);
	  object.addProperty("remarks",payerEmail);
	  object.addProperty("paymentCode",27);
	  
	  final CommandWrapper commandRequest = new CommandWrapperBuilder().createPaypal(payerEmail).withJson(object.toString()).build();
	  final CommandProcessingResult result1 = this.writePlatformService.logCommandSource(commandRequest);
	  return this.toApiJsonSerializer.serialize(result1); 
	        
	  } /*catch (PayPalException e) {
	    // TODO Auto-generated catch block   
	   return e.getMessage();
	       }*/
	   catch(Exception e){
	    return e.getMessage();
	   }
	 }
}
