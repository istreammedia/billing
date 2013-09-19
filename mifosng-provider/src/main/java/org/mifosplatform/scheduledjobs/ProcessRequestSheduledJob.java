package org.mifosplatform.scheduledjobs;

import java.util.Date;
import java.util.List;

import org.mifosplatform.billing.preparerequest.data.PrepareRequestData;
import org.mifosplatform.billing.preparerequest.service.PrepareRequestReadplatformService;
import org.mifosplatform.billing.processrequest.data.ProcessingDetailsData;
import org.mifosplatform.billing.processrequest.service.ProcessRequestReadplatformService;
import org.mifosplatform.billing.processscheduledjobs.service.SheduleJobWritePlatformService;
import org.mifosplatform.billing.scheduledjobs.ProcessRequestWriteplatformService;
import org.mifosplatform.infrastructure.core.domain.MifosPlatformTenant;
import org.mifosplatform.infrastructure.core.service.ThreadLocalContextUtil;
import org.mifosplatform.infrastructure.security.service.TenantDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessRequestSheduledJob  {

	
	  private final TenantDetailsService tenantDetailsService;
	  private final PrepareRequestReadplatformService prepareRequestReadplatformService;
	  private final ProcessRequestReadplatformService processRequestReadplatformService;
	  private final ProcessRequestWriteplatformService processRequestWriteplatformService;

	  @Autowired
	  private  SheduleJobWritePlatformService sheduleJobWritePlatformService;	  

	    @Autowired
	    public ProcessRequestSheduledJob(final TenantDetailsService tenantDetailsService,final ProcessRequestReadplatformService processRequestReadplatformService,
	    		final PrepareRequestReadplatformService prepareRequestReadplatformService,final ProcessRequestWriteplatformService processRequestWriteplatformService) {
	            this.tenantDetailsService = tenantDetailsService;
	            this.prepareRequestReadplatformService=prepareRequestReadplatformService;
	            this.processRequestReadplatformService=processRequestReadplatformService;
	            this.processRequestWriteplatformService=processRequestWriteplatformService;
	             
	    }
	   
		public void execute() {
			
	        final MifosPlatformTenant tenant = this.tenantDetailsService.loadTenantById("default"); 
 	        ThreadLocalContextUtil.setTenant(tenant);
 	        
	        //Retrieve the data from Prepare Request 
	          int processingJobs=0;
               List<PrepareRequestData> data=this.prepareRequestReadplatformService.retrieveDataForProcessing();
        
                    for(PrepareRequestData requestData:data){
            	
                                	this.prepareRequestReadplatformService.processingClientDetails(requestData);
                    }
                    
              //Retrieve the for Order Confirmation
                    List<ProcessingDetailsData> processingDetails=this.processRequestReadplatformService.retrieveProcessingDetails();
                    
                    for(ProcessingDetailsData detailsData:processingDetails){
                    	
                         this.processRequestWriteplatformService.notifyProcessingDetails(detailsData);	
                    }
                    
                            	    	
                    System.out.println("Processing  Request Jobs are "+processingJobs);
                    System.out.println("Running Job Processing Request"+new Date());
                    this.sheduleJobWritePlatformService.runSheduledJobs();
                    System.out.println("Finishing Job Processing Request"+new Date());
	    }
			
		}

		

