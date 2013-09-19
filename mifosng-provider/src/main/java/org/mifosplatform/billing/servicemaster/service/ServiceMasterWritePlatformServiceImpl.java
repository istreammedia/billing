package org.mifosplatform.billing.servicemaster.service;

import java.util.Map;

import org.mifosplatform.billing.servicemaster.domain.ProvisionServiceDetails;
import org.mifosplatform.billing.servicemaster.domain.ProvisionServiceDetailsRepository;
import org.mifosplatform.billing.servicemaster.domain.ServiceMaster;
import org.mifosplatform.billing.servicemaster.domain.ServiceMasterRepository;
import org.mifosplatform.billing.servicemaster.serialization.ServiceCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.codes.exception.CodeNotFoundException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ServiceMasterWritePlatformServiceImpl  implements ServiceMasterWritePlatformService{
	 private final static Logger logger = LoggerFactory.getLogger(ServiceMasterWritePlatformServiceImpl.class);
	private final PlatformSecurityContext context;
	private final ServiceMasterRepository serviceMasterRepository;
	private final ServiceCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final  ProvisionServiceDetailsRepository provisionServiceDetailsRepository;
@Autowired
 public ServiceMasterWritePlatformServiceImpl(final PlatformSecurityContext context,final ServiceMasterRepository serviceMasterRepository,
		 final ServiceCommandFromApiJsonDeserializer fromApiJsonDeserializer,final ProvisionServiceDetailsRepository provisionServiceDetailsRepository)
{
	this.context=context;
	this.serviceMasterRepository=serviceMasterRepository;
	this.fromApiJsonDeserializer=fromApiJsonDeserializer;
	this.provisionServiceDetailsRepository=provisionServiceDetailsRepository; 
}
    @Transactional
	@Override
	public CommandProcessingResult createNewService(final JsonCommand command) {
		try {
			context.authenticatedUser();
			   this.fromApiJsonDeserializer.validateForCreate(command.json());
			   final ServiceMaster serviceMaster = ServiceMaster.fromJson(command);
			   this.serviceMasterRepository.save(serviceMaster);
			  /* ProvisionServiceDetails provisionServiceDetails=new ProvisionServiceDetails(serviceMaster.getId(),"http://spark.openbillingsystem.com","Active","billing.jpg");
			   this.provisionServiceDetailsRepository.save(provisionServiceDetails);*/
			   
			   return new CommandProcessingResult(serviceMaster.getId());
		} catch (DataIntegrityViolationException dve) {
			 handleCodeDataIntegrityIssues(command, dve);
			return  CommandProcessingResult.empty();
		}
	}
    
	@Override
	public CommandProcessingResult updateService(Long id,JsonCommand command) {
		try
		{
			    context.authenticatedUser();
	            this.fromApiJsonDeserializer.validateForCreate(command.json());
	            final ServiceMaster master = retrieveCodeBy(id);
                final Map<String, Object> changes = master.update(command);
         if (!changes.isEmpty()) {
             this.serviceMasterRepository.save(master);
         }
         return new CommandProcessingResultBuilder() //
         .withCommandId(command.commandId()) //
         .withEntityId(id) //
         .with(changes) //
         .build();
	} catch (DataIntegrityViolationException dve) {
		 handleCodeDataIntegrityIssues(command, dve);
		return new CommandProcessingResult(Long.valueOf(-1));
	}
	}
	 private void handleCodeDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {
	        Throwable realCause = dve.getMostSpecificCause();
	        if (realCause.getMessage().contains("service_code_key")) {
	            final String name = command.stringValueOfParameterNamed("serviceCode");
	          //  throw new PlatformDataIntegrityException("error.msg.code.duplicate.name", "A code with name '" + name + "' already exists");
	            throw new PlatformDataIntegrityException("error.msg.code.duplicate.name", "A code with name'"
	                    + name + "'already exists", "displayName", name);
	        }

	        logger.error(dve.getMessage(), dve);
	        throw new PlatformDataIntegrityException("error.msg.cund.unknown.data.integrity.issue",
	                "Unknown data integrity issue with resource: " + realCause.getMessage());
	    }
	private ServiceMaster retrieveCodeBy(final Long serviceId) {
	        final ServiceMaster serviceMaster = this.serviceMasterRepository.findOne(serviceId);
	        if (serviceMaster == null) { throw new CodeNotFoundException(serviceId.toString()); }
	        return serviceMaster;
	    }
	 
	 
	@Override
	public CommandProcessingResult deleteService(Long serviceId) {
				
		    context.authenticatedUser();
	        final ServiceMaster serviceMaster = retrieveCodeBy(serviceId);
	        serviceMaster.delete();
			this.serviceMasterRepository.save(serviceMaster);
	        return new CommandProcessingResultBuilder().withEntityId(serviceId).build();
	    }

	}




