package org.mifosplatform.billing.scheduledjobs.service;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mifosplatform.billing.billingmaster.api.BillingMasterApiResourse;
import org.mifosplatform.billing.billingorder.service.InvoiceClient;
import org.mifosplatform.billing.message.service.BillingMessageDataWritePlatformService;
import org.mifosplatform.billing.order.data.OrderData;
import org.mifosplatform.billing.order.service.OrderReadPlatformService;
import org.mifosplatform.billing.order.service.OrderWritePlatformService;
import org.mifosplatform.billing.processscheduledjobs.service.SheduleJobReadPlatformService;
import org.mifosplatform.billing.processscheduledjobs.service.SheduleJobWritePlatformService;
import org.mifosplatform.billing.scheduledjobs.data.ScheduleJobData;
import org.mifosplatform.billing.scheduledjobs.domain.ScheduleJobs;
import org.mifosplatform.billing.scheduledjobs.domain.ScheduledJobRepository;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.jobs.annotation.CronTarget;
import org.mifosplatform.infrastructure.jobs.service.JobName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonElement;

@Service
public class SheduleJobWritePlatformServiceImpl  implements SheduleJobWritePlatformService{

	
	  private final SheduleJobReadPlatformService sheduleJobReadPlatformService;
	  private final InvoiceClient  invoiceClient;
	  private final ScheduledJobRepository scheduledJobRepository;
	  private  final BillingMasterApiResourse billingMasterApiResourse;  
	  private final OrderWritePlatformService orderWritePlatformService;
	  private final FromJsonHelper fromApiJsonHelper;
	  private final OrderReadPlatformService orderReadPlatformService;
	  private final BillingMessageDataWritePlatformService billingMessageDataWritePlatformService;
	    
	

	    @Autowired
	    public SheduleJobWritePlatformServiceImpl(final InvoiceClient invoiceClient,final SheduleJobReadPlatformService sheduleJobReadPlatformService,
	            final ScheduledJobRepository scheduledJobRepository,final BillingMasterApiResourse billingMasterApiResourse,
	            final OrderWritePlatformService orderWritePlatformService,final FromJsonHelper fromApiJsonHelper,
	            final OrderReadPlatformService orderReadPlatformService,final BillingMessageDataWritePlatformService billingMessageDataWritePlatformService ) {
	            this.sheduleJobReadPlatformService=sheduleJobReadPlatformService;
	            this.invoiceClient=invoiceClient;
	            this.scheduledJobRepository=scheduledJobRepository;
	            this.billingMasterApiResourse=billingMasterApiResourse;
	            this.orderWritePlatformService=orderWritePlatformService;
	            this.fromApiJsonHelper=fromApiJsonHelper;
	            this.orderReadPlatformService=orderReadPlatformService;
	            this.billingMessageDataWritePlatformService=billingMessageDataWritePlatformService;
	    }
	  
		@Override
		public void runSheduledJobs() {
         
			
        	   int sheduleJobs =0;
        	   List<ScheduleJobData> sheduleDatas=this.sheduleJobReadPlatformService.retrieveSheduleJobDetails();

        	   for(ScheduleJobData scheduleJobData : sheduleDatas){
        		   
        		   
        		    if(scheduleJobData.getProcessType().equalsIgnoreCase("Message")){
      				  try{
     					  Long messageId= this.sheduleJobReadPlatformService.getMessageId(scheduleJobData.getProcessParam());
                             this.billingMessageDataWritePlatformService.createMessageData(messageId,scheduleJobData.getQuery());
                            
                             
     				  } catch (Exception dve) {
         					 handleCodeDataIntegrityIssues(null, dve);
         				}
     				   }
        		    else{
        		    List<Long> clientIds=this.sheduleJobReadPlatformService.getClientIds(scheduleJobData.getQuery());
            		   //Get the Client Ids
                	   for(Long clientId : clientIds){
                		   try{
                			 
                			   
                			  if(scheduleJobData.getProcessType().equalsIgnoreCase("Invoice")){
            		         //   this.invoiceClient.invoicingSingleClient(clientId, new LocalDate());
            		            
            		            
                			   }else if(scheduleJobData.getProcessType().equalsIgnoreCase("Statement")){
                			       JSONObject jsonobject = new JSONObject();
                			       LocalDate date=new LocalDate();
                			        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");
                			        String formattedDate = formatter.print(date);
                			        
                			      //  System.out.println(formattedDate);
                			       jsonobject.put("dueDate",formattedDate.toString());
                			       jsonobject.put("locale", "en");
                	               jsonobject.put("dateFormat","dd MMMM YYYY");	
                		           jsonobject.put("message","Statment");
                		            this.billingMasterApiResourse.retrieveBillingProducts(clientId,jsonobject.toString());
                			 
                			   }else if(scheduleJobData.getProcessType().equalsIgnoreCase("Auto Expiry")){
                				   
                				   List<OrderData> orderDatas=this.orderReadPlatformService.retrieveClientOrderDetails(clientId);
                				   for(OrderData data : orderDatas){
                					   if(data.getEndDate().equals(new LocalDate())){
                						   
                						   JSONObject jsonobject = new JSONObject();
                        		           jsonobject.put("disconnectReason","Date Expired");
                        		           final JsonElement parsedCommand = this.fromApiJsonHelper.parse(jsonobject.toString());
                        		           
                        		           final JsonCommand command = JsonCommand.from(jsonobject.toString(),parsedCommand,this.fromApiJsonHelper,
                        		          		 "DissconnectOrder",clientId,null,null,clientId,null,null,null,null,null,null);
                        		           this.orderWritePlatformService.updateOrder(command,data.getId());
                						   
                					   }
                				   }
                				   
                			      
                			   }
                			   
                		   }
                			   catch (Exception dve) {
                					 handleCodeDataIntegrityIssues(null, dve);
                					//return  CommandProcessingResult.empty();
                				}
                		   sheduleJobs++;   
                          
                	   
        		 }
            	//   ScheduleJobs scheduleJob=this.scheduledJobRepository.findOne(scheduleJobData.getId()); 
            	   //     scheduleJob.setStatus('Y');
            	    //   this.scheduledJobRepository.save(scheduleJob);
            	       
            	       System.out.println("processing schedule Jobs are "+sheduleJobs);
        	   }   
		}
		}
		
		
@Transactional
@Override
@CronTarget(jobName = JobName.INVOICE)
public  void processInvoice() {
	
        System.out.println("Generating invoices for orders.....");	
	
	 List<ScheduleJobData> sheduleDatas=this.sheduleJobReadPlatformService.retrieveSheduleJobDetails();
	
	 for(ScheduleJobData scheduleJobData:sheduleDatas){
		 
	          List<Long> clientIds=this.sheduleJobReadPlatformService.getClientIds(scheduleJobData.getQuery());
         	   // Get the Client Ids
	             for(Long clientId : clientIds){
		           try{
			 
     	            this.invoiceClient.invoicingSingleClient(clientId, new LocalDate());
			         
		           }catch (Exception dve) {
				       handleCodeDataIntegrityIssues(null, dve);
		           }
	             }
	             ScheduleJobs scheduleJob=this.scheduledJobRepository.findOne(scheduleJobData.getId()); 
	  	       scheduleJob.setStatus('Y');
	  	      this.scheduledJobRepository.save(scheduleJob);
	 }
  
	 System.out.println("Invoices are Generated.....");
}
		private void handleCodeDataIntegrityIssues(Object object,Exception dve) {
			// TODO Auto-generated method stub
			
		}
		}

	


