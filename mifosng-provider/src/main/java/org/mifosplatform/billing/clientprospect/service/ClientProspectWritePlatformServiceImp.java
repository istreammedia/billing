package org.mifosplatform.billing.clientprospect.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.LocalDate;
import org.mifosplatform.billing.clientprospect.domain.ClientProspect;
import org.mifosplatform.billing.clientprospect.domain.ClientProspectJpaRepository;
import org.mifosplatform.billing.clientprospect.domain.ProspectDetail;
import org.mifosplatform.billing.clientprospect.domain.ProspectDetailJpaRepository;
import org.mifosplatform.billing.clientprospect.serialization.ClientProspectCommandFromApiJsonDeserializer;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.codes.exception.CodeNotFoundException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.portfolio.client.domain.ClientRepository;
import org.mifosplatform.portfolio.client.service.ClientWritePlatformService;
import org.mifosplatform.useradministration.domain.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientProspectWritePlatformServiceImp implements ClientProspectWritePlatformService{

	
	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(ClientProspectWritePlatformServiceImp.class);
	
	
	private final ClientWritePlatformService clientWritePlatformService;
	private final ClientRepository clientRepository;
	private final PlatformSecurityContext context;
	private final ClientProspectJpaRepository jpaRepository;
	private final ProspectDetailJpaRepository prospectDetailJpaRepository;
	private final ClientProspectCommandFromApiJsonDeserializer clientProspectCommandFromApiJsonDeserializer; 
	private final FromJsonHelper fromApiJsonHelper;
	  private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	
	
	@Autowired
	public ClientProspectWritePlatformServiceImp(final PlatformSecurityContext context, final ClientProspectJpaRepository jpaRepository, final ClientProspectCommandFromApiJsonDeserializer clientProspectCommandFromApiJsonDeserializer, final FromJsonHelper fromApiJsonHelper, final ProspectDetailJpaRepository prospectDetailJpaRepository, final ClientRepository clientRepository, final ClientWritePlatformService clientWritePlatformService, final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
		this.context = context;
		this.jpaRepository = jpaRepository;
		this.clientProspectCommandFromApiJsonDeserializer = clientProspectCommandFromApiJsonDeserializer;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.prospectDetailJpaRepository = prospectDetailJpaRepository;
		this.clientRepository = clientRepository;
		this.clientWritePlatformService = clientWritePlatformService;
		this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
	}
	
	@Transactional
	@Override
	public CommandProcessingResult createProspect(JsonCommand command){
		ClientProspect entity = null;
		try{
		context.authenticatedUser();
		clientProspectCommandFromApiJsonDeserializer.validateForCreate(command.json());
		entity = ClientProspect.fromJson(fromApiJsonHelper,command);
		jpaRepository.save(entity);
		return new CommandProcessingResultBuilder().withCommandId(entity.getId()).build();
		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command,dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}catch(ParseException pe){
			throw new PlatformDataIntegrityException("invalid.date.and.time.format", "invalid.date.and.time.format", "invalid.date.and.time.format");
		}
	}
	
	
	@Transactional
	@Override
	public CommandProcessingResult updateProspect(JsonCommand command, final Long prospectId) {
		try{
			context.authenticatedUser();
		clientProspectCommandFromApiJsonDeserializer.validateForUpdate(command.json());
		ProspectDetail prospectDetail = ProspectDetail.fromJson(command,prospectId);
		prospectDetailJpaRepository.save(prospectDetail);
		return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(prospectDetail.getId()).build();
		}catch(DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command, dve);
			return CommandProcessingResult.empty();
		} catch (ParseException e) {
			throw new PlatformDataIntegrityException("invalid.date.and.time.format", "invalid.date.and.time.format", "invalid.date.and.time.format");
		}
	}
	
	@Transactional
	@Override
	public CommandProcessingResult deleteProspect(JsonCommand command) {
        context.authenticatedUser();
        final ClientProspect clientProspect = retrieveCodeBy(command.entityId());
        clientProspect.setIsDeleted('Y');
        clientProspect.setStatus("Cancled");
        clientProspect.setStatusRemark(command.stringValueOfParameterNamed("statusRemark"));
        this.jpaRepository.saveAndFlush(clientProspect);
        return new CommandProcessingResultBuilder().withEntityId(clientProspect.getId()).build();
    }

    private ClientProspect retrieveCodeBy(final Long prospectId) {
        final ClientProspect clientProspect = this.jpaRepository.findOne(prospectId);
        if (clientProspect == null) { throw new CodeNotFoundException(prospectId.toString()); }
        return clientProspect;
    }
    
    
    @Override
    public CommandProcessingResult convertToClient(Long entityId) {
            
    	  final AppUser currentUser=context.authenticatedUser();
        final ClientProspect clientProspect = retrieveCodeBy(entityId);
        
        JSONObject newClientJsonObject = new JSONObject();
        JSONObject newClientAddressObject = new JSONObject();
        try {
        	SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
    		String  activationDate = formatter.format(new Date());
    		 
    		 final Long officeId = currentUser.getOffice().getId();
        	newClientJsonObject.put("dateFormat","dd MMMM yyyy");
        	newClientJsonObject.put("locale","en");
        	newClientJsonObject.put("officeId",officeId);
        	newClientJsonObject.put("firstname",clientProspect.getFirstName());
        	newClientJsonObject.put("middlename",clientProspect.getMiddleName());
			newClientJsonObject.put("lastname",clientProspect.getLastName());
			newClientJsonObject.put("fullname",""); 
			newClientJsonObject.put("externalId","");
			newClientJsonObject.put("clientCategory","20");
			//newClientJsonObject.put("active","300");
			newClientJsonObject.put("activationDate",activationDate);
			newClientJsonObject.put("active","true");
			newClientJsonObject.put("email",clientProspect.getEmail());
			newClientJsonObject.put("phone",clientProspect.getMobileNumber());
			
			
			
			newClientJsonObject.put("addressNo",clientProspect.getAddress());
			newClientJsonObject.put("street",clientProspect.getStreetArea());
			newClientJsonObject.put("city",clientProspect.getCityDistrict());
			newClientJsonObject.put("zipCode",clientProspect.getZipCode());
			newClientJsonObject.put("state", clientProspect.getState());
			newClientJsonObject.put("country",clientProspect.getCountry());
			
			
			
			
			final CommandWrapper commandNewClient = new CommandWrapperBuilder().createClient().withJson(newClientJsonObject.toString().toString()).build(); //
			final CommandProcessingResult clientResult = this.commandsSourceWritePlatformService.logCommandSource(commandNewClient);
			/*final CommandWrapper commandRequest = new CommandWrapperBuilder().createAddress(clientResult.getClientId()).withJson(newClientAddressObject.toString().toString()).build();
		    final CommandProcessingResult addressResult = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);*/
			
			clientProspect.setStatusRemark(clientResult.getClientId().toString());	
			
        
        } catch (JSONException e) {
			e.printStackTrace();
		}
        
        
        
        clientProspect.setStatus("Closed");
        //clientProspect.setIsDeleted('Y');
        
        
        
        //clientProspect.setStatusRemark(command.stringValueOfParameterNamed("statusRemark"));
        this.jpaRepository.saveAndFlush(clientProspect);
        return new CommandProcessingResultBuilder().withEntityId(clientProspect.getId()).build();
    	
    	
    }
    
    
	private void handleDataIntegrityIssues(final JsonCommand element, final DataIntegrityViolationException dve) {

		Throwable realCause = dve.getMostSpecificCause();
	     if (realCause.getMessage().contains("serial_no_constraint")){
	       	throw new PlatformDataIntegrityException("validation.error.msg.inventory.item.duplicate.serialNumber", "validation.error.msg.inventory.item.duplicate.serialNumber", "validation.error.msg.inventory.item.duplicate.serialNumber","");
	       }


	        logger.error(dve.getMessage(), dve);   	
	}
		
}
