package org.mifosplatform.billing.uploadstatus.api;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mifosplatform.billing.uploadstatus.command.UploadStatusCommand;
import org.mifosplatform.billing.uploadstatus.data.UploadStatusData;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatus;
import org.mifosplatform.billing.uploadstatus.service.UploadStatusReadPlatformService;
import org.mifosplatform.billing.uploadstatus.service.UploadStatusWritePlatformService;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiConstants;
import org.mifosplatform.infrastructure.core.api.ApiParameterHelper;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.serialization.ToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.service.FileUtils;
import org.mifosplatform.infrastructure.documentmanagement.command.DocumentCommand;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;



@Path("/uploadstatus")
@Component
@Scope("singleton")
public class UploadStatusApiResource {

	 private final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<String>(Arrays.asList("id", "name", "systemDefined","flag"));
	    private final String resourceNameForPermissions = "UPLOADSTATUS";
	    private static final Set<String> UPLOAD_STATUS_PARAMETERS = new HashSet<String>(Arrays.asList("id","uploadProcess","uploadFilePath","processDate",
	            "processRecords","processStatus","errorMessage","flag","unprocessRecords"));
	public InputStream inputStreamObject;
	
	
	@Autowired
	private UploadStatusWritePlatformService uploadStatusWritePlatformService;
	//@Autowired
	//private PortfolioApiDataBillingConversionService apiDataConversionService;
	
	
	//@Autowired
	//private PortfolioApiJsonBillingSerializerService apiJsonSerializerService;
	
	//private final DefaultToApiJsonSerializer<UploadStatusData> toApiJsonSerializerForItem;
	
	 private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

	private final ToApiJsonSerializer<UploadStatus> toApiJsonSerializer;
	private final PlatformSecurityContext context;
	@Autowired
    private final UploadStatusReadPlatformService readPlatformService;
    private final DefaultToApiJsonSerializer< UploadStatusData> defaulttoApiJsonSerializerforUploadStatus;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
	
    //ApiRequestJsonSerializationSettings settings
	 @Autowired
	    public UploadStatusApiResource(PlatformSecurityContext context, final UploadStatusReadPlatformService readPlatformService,
	            final DefaultToApiJsonSerializer<UploadStatusData> defaulttoApiJsonSerializerforUploadStatus, final ApiRequestParameterHelper apiRequestParameterHelper,final ToApiJsonSerializer<UploadStatus> toApiJsonSerializer
	            ,PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
	        this.context = context;
	        this.readPlatformService = readPlatformService;
	        this.defaulttoApiJsonSerializerforUploadStatus = defaulttoApiJsonSerializerforUploadStatus;
	        this.apiRequestParameterHelper = apiRequestParameterHelper;
	        this.toApiJsonSerializer=toApiJsonSerializer;
	        this.commandsSourceWritePlatformService=commandsSourceWritePlatformService;

	    }
	
	
	    

	 @GET
	 @Path("/getData")
	    @Consumes({ MediaType.APPLICATION_JSON })
	    @Produces({ MediaType.APPLICATION_JSON })
	    public String retrieveUploadFiles( @Context final UriInfo uriInfo) {
		 context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);

	        final Collection<UploadStatusData> codes = this.readPlatformService.retrieveAllCodes();
		 final Set<String> responseParameters = ApiParameterHelper.extractFieldsForResponseIfProvided(uriInfo.getQueryParameters());
			final boolean prettyPrint = ApiParameterHelper.prettyPrint(uriInfo.getQueryParameters());

			final List<UploadStatusData> uploadstatusdata= this.readPlatformService.retrieveAllUploadStatusData();			
			ApiRequestJsonSerializationSettings  settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
			return this.defaulttoApiJsonSerializerforUploadStatus.serialize(settings, uploadstatusdata, UPLOAD_STATUS_PARAMETERS);
			//return this.apiJsonSerializerService.serializeUploadStatusDataToJson(prettyPrint,responseParameters,uploadstatusdata);
	 
	    }
	 
	 @GET
	    @Consumes({ MediaType.APPLICATION_JSON })
	    @Produces({ MediaType.APPLICATION_JSON })
	    public String retrieveFile( @Context final UriInfo uriInfo) {

		 return null;
	    }
	 
	    @POST
	    @Path("/documents")
	    @Consumes({ MediaType.MULTIPART_FORM_DATA })
	    @Produces({ MediaType.APPLICATION_JSON })
	    public String createUploadFile(
	            @HeaderParam("Content-Length") Long fileSize, @FormDataParam("file") InputStream inputStream,
	            @FormDataParam("file") FormDataContentDisposition fileDetails, @FormDataParam("file") FormDataBodyPart bodyPart,
	            @FormDataParam("status") String name, @FormDataParam("description") String description) {

	        FileUtils.validateFileSizeWithinPermissibleRange(fileSize, name, ApiConstants.MAX_FILE_UPLOAD_SIZE_IN_MB);
	        int i;inputStreamObject=inputStream;
	        DocumentCommand documentCommand = new DocumentCommand(null, null, null, null, name, fileDetails.getFileName(), fileSize,
	        bodyPart.getMediaType().toString(), description, null);
	        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
	        Date date = new Date();
	        final DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMMM yyyy");
	        final LocalDate localdate = dtf.parseLocalDate(dateFormat.format(date));
	        
	        String fileUploadLocation = FileUtils.generateXlsFileDirectory();
	        String fileName=fileDetails.getFileName();
	        if (!new File(fileUploadLocation).isDirectory()) {
          new File(fileUploadLocation).mkdirs();
    
	        }
	        UploadStatusCommand uploadStatusCommand=new UploadStatusCommand(name,null,localdate,"",null,null,null,description,fileName,inputStream,fileUploadLocation);
	        CommandProcessingResult id = this.uploadStatusWritePlatformService.addItem(uploadStatusCommand);
      return null;
	 }
	 
	 
	
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response  addItemDetails(final String jsonRequestBody){
		final UploadStatusCommand command = uploadStatusWritePlatformService.convertJsonToUploadStatusCommand(null, jsonRequestBody);
		CommandProcessingResult id = this.uploadStatusWritePlatformService.addItem(command);
		return Response.ok().entity(id).build();
		
	}
	
	@PUT
	@Path("{uploadStatusId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response ProcessFile(@PathParam("uploadStatusId") final Long uploadStatusId, @Context final UriInfo uriInfo) {

		//OrdersCommand command =
		// this.apiDataConversionService.convertJsonToOrderCommand(null,null,jsonRequestBody);
		ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		CommandProcessingResult entityIdentifier = this.uploadStatusWritePlatformService.updateUploadStatus(uploadStatusId,settings);
		return Response.ok().entity(entityIdentifier).build();
		
	}
	
	
}
