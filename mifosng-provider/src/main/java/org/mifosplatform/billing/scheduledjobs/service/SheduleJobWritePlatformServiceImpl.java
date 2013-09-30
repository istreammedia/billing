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
import org.mifosplatform.billing.preparerequest.data.PrepareRequestData;
import org.mifosplatform.billing.preparerequest.service.PrepareRequestReadplatformService;
import org.mifosplatform.billing.processrequest.data.ProcessingDetailsData;
import org.mifosplatform.billing.processrequest.domain.ProcessRequest;
import org.mifosplatform.billing.processrequest.domain.ProcessRequestRepository;
import org.mifosplatform.billing.processrequest.service.ProcessRequestReadplatformService;
import org.mifosplatform.billing.processscheduledjobs.service.SheduleJobReadPlatformService;
import org.mifosplatform.billing.processscheduledjobs.service.SheduleJobWritePlatformService;
import org.mifosplatform.billing.scheduledjobs.ProcessRequestWriteplatformService;
import org.mifosplatform.billing.scheduledjobs.data.JobParameterData;
import org.mifosplatform.billing.scheduledjobs.data.ScheduleJobData;
import org.mifosplatform.billing.scheduledjobs.domain.ScheduleJobs;
import org.mifosplatform.billing.scheduledjobs.domain.ScheduledJobRepository;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.jobs.annotation.CronTarget;
import org.mifosplatform.infrastructure.jobs.domain.ScheduledJobDetailRepository;
import org.mifosplatform.infrastructure.jobs.service.JobName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonElement;

@Service
public class SheduleJobWritePlatformServiceImpl implements	SheduleJobWritePlatformService {

	private final SheduleJobReadPlatformService sheduleJobReadPlatformService;
	private final InvoiceClient invoiceClient;
	private final ScheduledJobRepository scheduledJobRepository;
	private final BillingMasterApiResourse billingMasterApiResourse;
	private final OrderWritePlatformService orderWritePlatformService;
	private final FromJsonHelper fromApiJsonHelper;
	private final OrderReadPlatformService orderReadPlatformService;
	private final BillingMessageDataWritePlatformService billingMessageDataWritePlatformService;
	private final PrepareRequestReadplatformService prepareRequestReadplatformService;
	private final ProcessRequestReadplatformService processRequestReadplatformService;
	private final ProcessRequestWriteplatformService processRequestWriteplatformService;
	private final ProcessRequestRepository processRequestRepository;
	private final ScheduledJobDetailRepository scheduledJobDetailRepository;
	

	@Autowired
	public SheduleJobWritePlatformServiceImpl(final InvoiceClient invoiceClient,final SheduleJobReadPlatformService sheduleJobReadPlatformService,
			final ScheduledJobRepository scheduledJobRepository,final BillingMasterApiResourse billingMasterApiResourse,final ProcessRequestRepository processRequestRepository,
			final OrderWritePlatformService orderWritePlatformService,final FromJsonHelper fromApiJsonHelper,final OrderReadPlatformService orderReadPlatformService,
			final BillingMessageDataWritePlatformService billingMessageDataWritePlatformService,final PrepareRequestReadplatformService prepareRequestReadplatformService,
			final ProcessRequestReadplatformService processRequestReadplatformService,final ProcessRequestWriteplatformService processRequestWriteplatformService,
			final ScheduledJobDetailRepository scheduledJobDetailRepository)
	{
		this.sheduleJobReadPlatformService = sheduleJobReadPlatformService;
		this.invoiceClient = invoiceClient;
		this.scheduledJobRepository = scheduledJobRepository;
		this.billingMasterApiResourse = billingMasterApiResourse;
		this.orderWritePlatformService = orderWritePlatformService;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.orderReadPlatformService = orderReadPlatformService;
		this.billingMessageDataWritePlatformService = billingMessageDataWritePlatformService;
		this.prepareRequestReadplatformService = prepareRequestReadplatformService;
		this.processRequestReadplatformService = processRequestReadplatformService;
		this.processRequestWriteplatformService = processRequestWriteplatformService;
		this.processRequestRepository = processRequestRepository;
		this.scheduledJobDetailRepository=scheduledJobDetailRepository;
	}

	

	@Transactional
	@Override
	@CronTarget(jobName = JobName.INVOICE)
	public void processInvoice() {

	try
	{
		
		JobParameterData data=this.sheduleJobReadPlatformService.getJobParameters(JobName.INVOICE.toString());
		
		if(data!=null){
			
		    	List<ScheduleJobData> sheduleDatas = this.sheduleJobReadPlatformService.retrieveSheduleJobDetails(data.getBatchName());
		    	    	 
		    	    	 for (ScheduleJobData scheduleJobData : sheduleDatas) {

		    	 			List<Long> clientIds = this.sheduleJobReadPlatformService.getClientIds(scheduleJobData.getQuery());
		    	 			
		    	 			// Get the Client Ids
		    	 			for (Long clientId : clientIds) {
		    	 				try {

		    	 					this.invoiceClient.invoicingSingleClient(clientId,data.getProcessDate());

		    	 				} catch (Exception dve) {
		    	 					handleCodeDataIntegrityIssues(null, dve);
		    	 				}
		    	 			}
		    	 			ScheduleJobs scheduleJob = this.scheduledJobRepository
		    	 					.findOne(scheduleJobData.getId());
		    	 			scheduleJob.setStatus('Y');
		    	 			this.scheduledJobRepository.save(scheduleJob);
		    	 		}

		    	 		System.out.println("Invoices are Generated.....");
		    	
		    }
	
	}catch(DataIntegrityViolationException exception)
	{
		exception.printStackTrace();
	}
	
	
	}

	private void handleCodeDataIntegrityIssues(Object object, Exception dve) {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	@CronTarget(jobName = JobName.REQUESTOR)
	public void processRequest() {

		try {
			
			System.out.println("Processing Request Details.......");
			
			List<PrepareRequestData> data = this.prepareRequestReadplatformService.retrieveDataForProcessing();

			for (PrepareRequestData requestData : data) {

				this.prepareRequestReadplatformService.processingClientDetails(requestData);
			}

			System.out.println(" Requestor Job is Completed....");

		} catch (DataIntegrityViolationException exception) {

		}
	}

	@Transactional
	@Override
	@CronTarget(jobName = JobName.RESPONSOR)
	public void processResponse() {

		try {
			System.out.println("Processing Response Details.......");
			
			List<ProcessingDetailsData> processingDetails = this.processRequestReadplatformService.retrieveProcessingDetails();

			for (ProcessingDetailsData detailsData : processingDetails) {

				this.processRequestWriteplatformService.notifyProcessingDetails(detailsData);
			}
			System.out.println("Responsor Job is Completed...");
			
		} catch (DataIntegrityViolationException exception) {

		}
	}

	@Transactional
	@Override
	@CronTarget(jobName = JobName.SIMULATOR)
	public void processSimulator() {

		try {
			System.out.println("Processing Simulator Details.......");
			
			List<ProcessingDetailsData> processingDetails = this.processRequestReadplatformService.retrieveUnProcessingDetails();

			for (ProcessingDetailsData detailsData : processingDetails) {

				ProcessRequest processRequest = this.processRequestRepository.findOne(detailsData.getId());
				
				processRequest.setProcessStatus();
				this.processRequestRepository.save(processRequest);
				
				
			}
			System.out.println("Responsor Job is Completed...");
		} catch (DataIntegrityViolationException exception) {

		}
	}
	
	
	@Override
	@CronTarget(jobName = JobName.GENERATE_STATMENT)
	public void generateStatment() 
	  {

		try {
			
			JobParameterData data=this.sheduleJobReadPlatformService.getJobParameters(JobName.GENERATE_STATMENT.toString());
		    if(data!=null){
		    	 
		    	 List<ScheduleJobData> sheduleDatas = this.sheduleJobReadPlatformService.retrieveSheduleJobDetails(data.getBatchName());
		    	 
		    	for(ScheduleJobData scheduleJobData:sheduleDatas)
				{
					List<Long> clientIds = this.sheduleJobReadPlatformService.getClientIds(scheduleJobData.getQuery());
					  
					 for(Long clientId:clientIds)
					 {
						
						 JSONObject jsonobject = new JSONObject();
						
							DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");
							String formattedDate = formatter.print(data.getDueDate());

							// System.out.println(formattedDate);
							jsonobject.put("dueDate",formattedDate);
							jsonobject.put("locale", "en");
							jsonobject.put("dateFormat", "dd MMMM YYYY");
							jsonobject.put("message", data.getPromotionalMessage());
							this.billingMasterApiResourse.retrieveBillingProducts(clientId,	jsonobject.toString());
					 }
				}
				
		}
		    System.out.println("Job is Completed...");
		}catch (Exception exception) {

		}
	}	
	
	@Transactional
	@Override
	@CronTarget(jobName = JobName.MESSANGER)
	public void processingMessages() 
	  {
		try 
		{
			JobParameterData data=this.sheduleJobReadPlatformService.getJobParameters(JobName.MESSANGER.toString());
	         
            if(data!=null){
      			
		List<ScheduleJobData> sheduleDatas = this.sheduleJobReadPlatformService.retrieveSheduleJobDetails(data.getBatchName());
		
		for (ScheduleJobData scheduleJobData : sheduleDatas) {

					Long messageId = this.sheduleJobReadPlatformService.getMessageId(data.getMessageTempalate());
					
					this.billingMessageDataWritePlatformService.createMessageData(messageId,scheduleJobData.getQuery());

				}
		    }
		}
		
		catch (Exception dve) 
		{
					handleCodeDataIntegrityIssues(null, dve);
		}
	  }
	
	@Transactional
	@Override
	@CronTarget(jobName = JobName.AUTO_EXIPIRY)
	public void processingAutoExipryOrders() 
	  {
		try 
		{
			
			
			JobParameterData data=this.sheduleJobReadPlatformService.getJobParameters(JobName.AUTO_EXIPIRY.toString());
         
                 if(data!=null){
                	 
			List<ScheduleJobData> sheduleDatas = this.sheduleJobReadPlatformService.retrieveSheduleJobDetails(data.getBatchName());
			
			for (ScheduleJobData scheduleJobData : sheduleDatas) 
			{
				List<Long> clientIds = this.sheduleJobReadPlatformService.getClientIds(scheduleJobData.getQuery());
				for(Long clientId:clientIds)
				{
					
				List<OrderData> orderDatas = this.orderReadPlatformService.retrieveClientOrderDetails(clientId);
				
      			for (OrderData orderData : orderDatas) 
      			  {
      		
				    if (orderData.getEndDate().equals(new LocalDate()))
				     {

					JSONObject jsonobject = new JSONObject();
					jsonobject.put("disconnectReason","Date Expired");
					final JsonElement parsedCommand = this.fromApiJsonHelper.parse(jsonobject.toString());

					final JsonCommand command = JsonCommand.from(jsonobject.toString(),parsedCommand,this.fromApiJsonHelper,"DissconnectOrder",clientId, null,
							null,clientId, null, null, null,null, null, null);
					this.orderWritePlatformService.disconnectOrder(command,	orderData.getId());
				     }
				}
			}
				}
		}
		
		   
		}
		catch (Exception dve) 
		{
					handleCodeDataIntegrityIssues(null, dve);
		}
	  }	
	@Transactional
	@Override
	@CronTarget(jobName = JobName.ALL)
	public void newJob() 
	  {
		System.out.println("This is new Job");
	  }	 		
}
