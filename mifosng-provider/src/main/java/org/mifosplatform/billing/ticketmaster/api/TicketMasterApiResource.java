package org.mifosplatform.billing.ticketmaster.api;

import java.io.File;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.billing.ticketmaster.data.ClientTicketData;
import org.mifosplatform.billing.ticketmaster.data.ProblemsData;
import org.mifosplatform.billing.ticketmaster.data.TicketMasterData;
import org.mifosplatform.billing.ticketmaster.data.UsersData;
import org.mifosplatform.billing.ticketmaster.domain.TicketDetail;
import org.mifosplatform.billing.ticketmaster.repository.TicketDetailsRepository;
import org.mifosplatform.billing.ticketmaster.service.TicketMasterReadPlatformService;
import org.mifosplatform.billing.ticketmaster.service.TicketMasterWritePlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiParameterHelper;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Path("/tickets")
@Component
@Scope("singleton")
public class TicketMasterApiResource {


		private TicketMasterWritePlatformService ticketMasterWritePlatformService;
		private TicketMasterReadPlatformService ticketMasterReadPlatformService ;
		private  DefaultToApiJsonSerializer<TicketMasterData> toApiJsonSerializer;
		private  DefaultToApiJsonSerializer<ClientTicketData> clientToApiJsonSerializer;
		private ApiRequestParameterHelper apiRequestParameterHelper;
		private TicketDetailsRepository detailsRepository;
		private PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
		private PlatformSecurityContext context;
		
		@Autowired
		public TicketMasterApiResource(final TicketMasterWritePlatformService ticketMasterWritePlatformService,final TicketMasterReadPlatformService ticketMasterReadPlatformService,
										final DefaultToApiJsonSerializer<TicketMasterData> toApiJsonSerializer, final DefaultToApiJsonSerializer<ClientTicketData> clientToApiJsonSerializer,
										final ApiRequestParameterHelper apiRequestParameterHelper, final TicketDetailsRepository detailsRepository,
										final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService, final PlatformSecurityContext context)	{
			this.ticketMasterWritePlatformService = ticketMasterWritePlatformService;
			this.ticketMasterReadPlatformService = ticketMasterReadPlatformService;
		    this.toApiJsonSerializer = toApiJsonSerializer;
			this.clientToApiJsonSerializer = clientToApiJsonSerializer;
			this.apiRequestParameterHelper = apiRequestParameterHelper;
			this.detailsRepository = detailsRepository;
			this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
			this.context = context;
		}
		private final String entityType = "CLIENT";
		private final Set<String> RESPONSE_PARAMETERS = new HashSet<String>(Arrays.asList("id","priorityType","problemsDatas","usersData","status","assignedTo","userName","ticketDate","lastComment","masterData"));   

		

		/**
		 * Method To Create TicketMaster 
		 * @param clientId
		 * @param jsonRequestBody
		 * @return String Json Result 
		 */
		@POST
	    @Path("{clientId}")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String createTicketMaster(@PathParam("clientId") final Long clientId,final String jsonRequestBody) {

			final CommandWrapper commandRequest = new CommandWrapperBuilder().createTicketMaster(clientId).withJson(jsonRequestBody).build();
			final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
			
			return this.toApiJsonSerializer.serialize(result);
		}

		/**
		 * Method for Retrieving Ticket Details for template
		 * @param uriInfo
		 * @return
		 */
		@GET
		@Path("template")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String retrieveTicketMasterTemplateData(@Context final UriInfo uriInfo) {

			context.authenticatedUser().validateHasReadPermission(entityType);

			Set<String> typicalResponseParameters = new HashSet<String>(Arrays.asList("statusType"));

			Set<String> responseParameters = ApiParameterHelper.extractFieldsForResponseIfProvided(uriInfo.getQueryParameters());
			if (responseParameters.isEmpty()) {
				responseParameters.addAll(typicalResponseParameters);
			}
			responseParameters.addAll(RESPONSE_PARAMETERS);
			TicketMasterData templateData = handleTemplateRelatedData(responseParameters,null);
			final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
			return this.toApiJsonSerializer.serialize(settings, templateData, responseParameters);
		}
		
		/**
		 * Method for retrieve Single Ticket for clientId 
		 * @param clientId
		 * @param uriInfo
		 * @return
		 */
		@GET
	    @Path("{clientId}")
	    @Consumes({MediaType.APPLICATION_JSON})
	    @Produces({MediaType.APPLICATION_JSON})
	    public String retrieveSingleClientTicketDetails(@PathParam("clientId") final Long clientId, @Context final UriInfo uriInfo) {



	        final List<TicketMasterData> data = this.ticketMasterReadPlatformService.retrieveClientTicketDetails(clientId);
	        
	        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	        return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_PARAMETERS);
	    }
		   
	    /**
	     * Method to get TicketDetails for update
	     * @param clientId
	     * @param ticketId
	     * @param uriInfo
	     * @return
	     */
	    @GET
	    @Path("{clientId}/update/{ticketId}")
	    @Consumes({MediaType.APPLICATION_JSON})
	    @Produces({MediaType.APPLICATION_JSON})
	    public String retrieveClientSingleTicketDetails(@PathParam("clientId")final Long clientId ,@PathParam("ticketId") final Long ticketId, @Context final UriInfo uriInfo) {
	  
	    	TicketMasterData data = this.ticketMasterReadPlatformService.retrieveSingleTicketDetails(clientId,ticketId);
	         data = handleTemplateRelatedData(RESPONSE_PARAMETERS,data);
	         List<TicketMasterData> Statusdata = this.ticketMasterReadPlatformService.retrieveTicketStatusData();
	         data.setStatusData(Statusdata);
	         final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	         return this.toApiJsonSerializer.serialize(settings, data, RESPONSE_PARAMETERS);
	    }
	   
		/**
		 * Method to Close Ticket
		 * @param ticketId
		 * @param jsonRequestBody
		 * @return
		 */
		@PUT
		@Path("{ticketId}")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String closeTicket(@PathParam("ticketId") final Long ticketId,final String jsonRequestBody) {
			final CommandWrapper commandRequest = new CommandWrapperBuilder().closeTicketMaster(ticketId).withJson(jsonRequestBody).build();
			final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
			return this.toApiJsonSerializer.serialize(result);
		}
		
		/**
		 * Method to retrieve Ticket History
		 * @param ticketId
		 * @param uriInfo
		 * @return
		 */
		@GET
	    @Path("{ticketId}/history")
	    @Consumes({MediaType.APPLICATION_JSON})
	    @Produces({MediaType.APPLICATION_JSON})
	    public String ticketHistory(@PathParam("ticketId") final Long ticketId, @Context final UriInfo uriInfo) {

			 String description=this.ticketMasterWritePlatformService.retrieveTicketProblems(ticketId);
	         final List<TicketMasterData> data = this.ticketMasterReadPlatformService.retrieveClientTicketHistory(ticketId);
	        
	        TicketMasterData masterData=new TicketMasterData(description,data);
             
	         final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	         return this.toApiJsonSerializer.serialize(settings, masterData, RESPONSE_PARAMETERS);
	        
	    }
		
		/**
		 * Method to download file and print if exists
		 * @param ticketId
		 * @return
		 */
		@GET
		@Path("{ticketId}/print")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public Response downloadFile(@PathParam("ticketId") final Long ticketId) {

			TicketDetail ticketDetail = this.detailsRepository.findOne(ticketId);

			String printFileName = ticketDetail.getAttachments();

			File file = new File(printFileName);
			ResponseBuilder response = Response.ok(file);
			response.header("Content-Disposition", "attachment; filename=\""+ printFileName + "\"");
			response.header("Content-Type", "application/pdf");
			return response.build();
		}
		
		/**
		 * Method to get all the tickets and associated for user
		 * @param uriInfo
		 * @return
		 */
		@GET
		@Path("assignedTickets")
		@Consumes({ MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_JSON })
		public String assignedTicketsForUser(@Context final UriInfo uriInfo){
		
		    final List<ClientTicketData> data = this.ticketMasterReadPlatformService.retrieveAssignedTickets();
		    final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	         return this.clientToApiJsonSerializer.serialize(settings, data, RESPONSE_PARAMETERS);
		}
	
		
		/**
		 * Method to handle Template Data
		 * @param responseParameters
		 * @param singleTicketData
		 * @return
		 */
		private TicketMasterData handleTemplateRelatedData(final Set<String> responseParameters,TicketMasterData singleTicketData) {
			List<EnumOptionData> priorityData = this.ticketMasterReadPlatformService.retrievePriorityData();
			List<TicketMasterData> closedStatusdata = this.ticketMasterReadPlatformService.retrieveTicketCloseStatusData();
			List<ProblemsData> datas=this.ticketMasterReadPlatformService.retrieveProblemData();
			List<UsersData>  userData=this.ticketMasterReadPlatformService.retrieveUsers();
			singleTicketData= new TicketMasterData(closedStatusdata,datas,userData,singleTicketData,priorityData);
			
			return  singleTicketData;
		}
}

