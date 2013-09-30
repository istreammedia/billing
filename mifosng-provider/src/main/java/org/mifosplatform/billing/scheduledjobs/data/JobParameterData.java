package org.mifosplatform.billing.scheduledjobs.data;

import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.scheduledjobs.domain.JobParameters;
import org.mifosplatform.billing.scheduledjobs.service.JobParametersConstants;

public class JobParameterData {
	
	private String batchName;
	private String promotionalMessage;
	private String messageTempalate;
	private char isDynamic;
	private LocalDate dueDate;
	private LocalDate processDate;

	public JobParameterData(List<JobParameters> jobParameters) {
              
		for(JobParameters parameter:jobParameters){
			
			if(parameter.getParamName().equalsIgnoreCase(JobParametersConstants.PARAM_BATCH)){
				     this.batchName=parameter.getParamValue();
			
			}else if(parameter.getParamName().equalsIgnoreCase(JobParametersConstants.PARAM_PROMTIONALMESSAGE)){
			          this.promotionalMessage=parameter.getParamValue();	
			
			}else if(parameter.getParamName().equalsIgnoreCase(JobParametersConstants.PARAM_MESSAGETEMPLATE)){
				     this.messageTempalate=parameter.getParamValue();
			} 
			
			if(parameter.isDynamic() == 'Y' ){
	    		  if(parameter.getParamValue().equalsIgnoreCase("+1")){
	    			  
	    			  this.dueDate=new LocalDate().plusDays(1);
	    			  this.processDate=new LocalDate().plusDays(1);
	    		  }else{
	    			  dueDate=new LocalDate().minusDays(1);
	    			  this.processDate=new LocalDate().minusDays(1);
	    		  }
	    	          	  
	    	  }
		}
		
		
	}

	public String getBatchName() {
		return batchName;
	}

	public String getPromotionalMessage() {
		return promotionalMessage;
	}

	public String getMessageTempalate() {
		return messageTempalate;
	}

	public char isDynamic() {
		return isDynamic;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public LocalDate getProcessDate() {
		return processDate;
	}
	
	

}
