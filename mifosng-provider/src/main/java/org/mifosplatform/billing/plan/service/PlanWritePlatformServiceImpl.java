package org.mifosplatform.billing.plan.service;

import java.util.List;
import java.util.Map;

import org.mifosplatform.billing.action.data.VolumeDetailsData;
import org.mifosplatform.billing.action.service.EventActionReadPlatformService;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.plan.domain.Plan;
import org.mifosplatform.billing.plan.domain.PlanDetails;
import org.mifosplatform.billing.plan.domain.PlanHardWareDetails;
import org.mifosplatform.billing.plan.domain.PlanHardWareDetailsRepository;
import org.mifosplatform.billing.plan.domain.PlanRepository;
import org.mifosplatform.billing.plan.domain.VolumeDetails;
import org.mifosplatform.billing.plan.domain.VolumeDetailsRepository;
import org.mifosplatform.billing.plan.serialization.PlanCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.servicemaster.domain.ServiceMaster;
import org.mifosplatform.billing.servicemaster.domain.ServiceMasterRepository;
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

import com.google.gson.JsonArray;


@Service
public class PlanWritePlatformServiceImpl implements PlanWritePlatformService {
	 private final static Logger logger = LoggerFactory.getLogger(PlanWritePlatformServiceImpl.class);
	private final PlatformSecurityContext context;
	private final PlanRepository planRepository;
	private final ServiceMasterRepository serviceMasterRepository;
	private final PlanCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final PlanReadPlatformService planReadPlatformService;
	private final VolumeDetailsRepository volumeDetailsRepository;
	private final EventActionReadPlatformService eventActionReadPlatformService;
	private final PlanHardWareDetailsRepository planHardWareDetailsRepository;
	@Autowired
	public PlanWritePlatformServiceImpl(final PlatformSecurityContext context,PlanHardWareDetailsRepository planHardWareDetailsRepository,
			final PlanRepository planRepository,final PlanReadPlatformService planReadPlatformService,
			final ServiceMasterRepository serviceMasterRepository,final VolumeDetailsRepository volumeDetailsRepository,
			final PlanCommandFromApiJsonDeserializer fromApiJsonDeserializer,final EventActionReadPlatformService eventActionReadPlatformService) {
		this.context = context;
		this.planRepository = planRepository;
		this.serviceMasterRepository =serviceMasterRepository;
		this.fromApiJsonDeserializer=fromApiJsonDeserializer;
		this.planReadPlatformService=planReadPlatformService;
		this.volumeDetailsRepository=volumeDetailsRepository;
		this.eventActionReadPlatformService=eventActionReadPlatformService;
		this.planHardWareDetailsRepository=planHardWareDetailsRepository;

	}

	@Override
	public CommandProcessingResult createPlan(JsonCommand command) {

		try {

			 this.context.authenticatedUser();
		     this.fromApiJsonDeserializer.validateForCreate(command.json());
			  Plan plan=Plan.fromJson(command);
			 final JsonArray array=command.arrayOfParameterNamed("services").getAsJsonArray();
			 
			    String[] service =null;
			    service=new String[array.size()];
			    for(int i=0; i<array.size();i++){
			    	service[i] =array.get(i).getAsString();
			    }
			   
				 for (String serviceId : service) {
		                final Long id = Long.valueOf(serviceId);
		                ServiceMaster service1 = this.serviceMasterRepository.findOne(id);
		                PlanDetails detail=new PlanDetails(service1.getServiceCode());
		                plan.addServieDetails(detail);
				  }
         this.planRepository.save(plan);
         
        /* PlanHardWareDetails hardWareDetails=new PlanHardWareDetails(plan.getId(),'N',"None",plan.getPlanCode()); 
         this.planHardWareDetailsRepository.save(hardWareDetails);*/
         
         if(plan.isPrepaid() =='Y'){
        	 VolumeDetails volumeDetails=VolumeDetails.fromJson(command,plan);
        	 this.volumeDetailsRepository.save(volumeDetails);
        	 
         }
			return new CommandProcessingResult(Long.valueOf(-1));

		} catch (DataIntegrityViolationException dve) {
			 handleCodeDataIntegrityIssues(command, dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}
	}

	private void handleCodeDataIntegrityIssues(JsonCommand command,
			DataIntegrityViolationException dve) {
		 Throwable realCause = dve.getMostSpecificCause();
	        if (realCause.getMessage().contains("plan_code_key")) {
	            final String name = command.stringValueOfParameterNamed("serviceCode");
	            throw new PlatformDataIntegrityException("error.msg.code.duplicate.name", "A code with name '" + name + "' already exists");
	        }

	        logger.error(dve.getMessage(), dve);
	        throw new PlatformDataIntegrityException("error.msg.cund.unknown.data.integrity.issue",
	                "Unknown data integrity issue with resource: " + realCause.getMessage());
		
	}

	@Override
	public CommandProcessingResult updatePlan(Long planId, JsonCommand command) {
		try
		{
			
			  context.authenticatedUser();
	            this.fromApiJsonDeserializer.validateForCreate(command.json());
	            final Plan plan = retrievePlanBy(planId);
	          //  PlanData serviceData = this.planReadPlatformService.retrievePlanData(planId);
	    		List<ServiceData> services = this.planReadPlatformService.retrieveSelectedServices(planId);
                   
 			for(ServiceData data:services)
 			{
 				plan.getDetails().clear();
 				final JsonArray array=command.arrayOfParameterNamed("services").getAsJsonArray();
			    String[] service =null;
			    service=new String[array.size()];
			    for(int i=0; i<array.size();i++){
			    	service[i] =array.get(i).getAsString();
			    }
			   
				 for (String serviceId : service) {
		                final Long id = Long.valueOf(serviceId);
		                ServiceMaster service1 = this.serviceMasterRepository.findOne(id);
		                PlanDetails detail=new PlanDetails(service1.getServiceCode());
		                plan.addServieDetails(detail);
				  }
 				
 			}
 		  final Map<String, Object> changes = plan.update(command);
             this.planRepository.save(plan);
             
         
             
             if(plan.isPrepaid()!='N'){
            	 VolumeDetailsData detailsData=this.eventActionReadPlatformService.retrieveVolumeDetails(plan.getId());
            	 VolumeDetails volumeDetails=new VolumeDetails();
            	 if(detailsData!=null){
            	  volumeDetails=new VolumeDetails(detailsData.getId(),detailsData.getPlanId(),detailsData.getVolumeType(),
            			 detailsData.getUnits(),detailsData.getUnitType());
            	 }
            	
            	 final Map<String, Object> volumeChanges = volumeDetails.update(command,planId);
            	   
            	 this.volumeDetailsRepository.save(volumeDetails);
             }

             return new CommandProcessingResultBuilder() //
         .withCommandId(command.commandId()) //
         .withEntityId(planId) //
         .with(changes) //
         .build();
	} catch (DataIntegrityViolationException dve) {
		 handleCodeDataIntegrityIssues(command, dve);
		return new CommandProcessingResult(Long.valueOf(-1));
	}
	}

	private Plan retrievePlanBy(Long planId) {
		  final Plan plan = this.planRepository.findOne(planId);
	        if (plan == null) { throw new CodeNotFoundException(planId.toString()); }
	        return plan;
	}

	public CommandProcessingResult deleteplan(Long planId) {
		List<ServiceData> services = this.planReadPlatformService.retrieveSelectedServices(planId);
		 Plan plan=this.planRepository.findOne(planId);
	    for(ServiceData data:services)
			{
				 PlanDetails details=new PlanDetails(data.getPlanCode());
				 plan.addServieDetails(details);
				 details.delete();
			}
		 plan.delete();
		 this.planRepository.save(plan);
		  return new CommandProcessingResultBuilder().withEntityId(planId).build();
	}

	
	


}
