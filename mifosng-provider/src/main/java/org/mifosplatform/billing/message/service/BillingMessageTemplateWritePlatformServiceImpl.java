package org.mifosplatform.billing.message.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.mifosplatform.billing.message.data.BillingMessageData;
import org.mifosplatform.billing.message.domain.BillingMessageParam;
import org.mifosplatform.billing.message.domain.BillingMessageParamRepository;
import org.mifosplatform.billing.message.domain.BillingMessageTemplate;
import org.mifosplatform.billing.message.domain.BillingMessageTemplateRepository;
import org.mifosplatform.billing.message.serialization.BillingMessageTemplateCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.paymode.exception.PaymodeNotFoundException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

@Service
public class BillingMessageTemplateWritePlatformServiceImpl implements BillingMessageTemplateWritePlatformService{

	    private final PlatformSecurityContext context;
	    private final FromJsonHelper fromApiJsonHelper;
	    private final BillingMessageTemplateRepository billingMessageTemplateRepository;
	    private final BillingMessageParamRepository billingMessageParamRepository;
	    private final BillingMessageTemplateCommandFromApiJsonDeserializer billingMessageTemplateCommandFromApiJsonDeserializer;
	   
	    @Autowired
	    public BillingMessageTemplateWritePlatformServiceImpl(PlatformSecurityContext context,
	    	    FromJsonHelper fromApiJsonHelper,BillingMessageParamRepository billingMessageParamRepository,
	    		BillingMessageTemplateRepository billingMessageTemplateRepository, 
	    		BillingMessageTemplateCommandFromApiJsonDeserializer billingMessageTemplateCommandFromApiJsonDeserializer)
	    {
	    	this.context=context;
	    	this.fromApiJsonHelper=fromApiJsonHelper;
	    	this.billingMessageParamRepository=billingMessageParamRepository;
	    	this.billingMessageTemplateRepository=billingMessageTemplateRepository;
	    	this.billingMessageTemplateCommandFromApiJsonDeserializer=billingMessageTemplateCommandFromApiJsonDeserializer;
	    }
	
	    
	 //for post method
	    @Override
	    public CommandProcessingResult addMessageTemplate(JsonCommand command) {
			
			try {
				context.authenticatedUser();
				this.billingMessageTemplateCommandFromApiJsonDeserializer.validateForCreate(command.json());
				 BillingMessageTemplate  billingMessageTemplate = BillingMessageTemplate.fromJson(command);
				 this.billingMessageTemplateRepository.save(billingMessageTemplate);
				
	            final JsonArray billingMessageparamArray=command.arrayOfParameterNamed("messageParams").getAsJsonArray();
				  String[] messageparam =null;
				  messageparam=new String[billingMessageparamArray.size()];
				  
				  for(int i=0; i<billingMessageparamArray.size();i++){
					  
					  messageparam[i] =billingMessageparamArray.get(i).toString();
				       
				  }
				  if(messageparam.length>0){
					  
				  Long number=new Long(1);
					 for (String messageparamdetails : messageparam)
					 { 
				     final JsonElement element = fromApiJsonHelper.parse(messageparamdetails);
					 final String parameterName = fromApiJsonHelper.extractStringNamed("parameter", element);
					 Long sequenceNo=number;
					 BillingMessageParam billingMessageParam = new BillingMessageParam(sequenceNo, parameterName);
					 billingMessageTemplate.setBillingMessageParam(billingMessageParam);
					 this.billingMessageParamRepository.save(billingMessageParam);
					 number++;
			          }
					
				  }
	           
	            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(billingMessageTemplate.getId()).build();
			    
	        
			} catch (DataIntegrityViolationException dve) {
	            return CommandProcessingResult.empty();
	        }
			
		}

	    //for put method
		@Override
		public CommandProcessingResult updateMessageTemplate(JsonCommand command) {
			try {
				context.authenticatedUser();
				this.billingMessageTemplateCommandFromApiJsonDeserializer.validateForCreate(command.json());
				final BillingMessageTemplate messageTemplate=retriveMessageBy(command.entityId());
				final Map<String, Object> changes = messageTemplate.updateMessageTemplate(command);
				messageTemplate.getDetails().clear();
				
				final JsonArray billingMessageparamArray=command.arrayOfParameterNamed("messageParams").getAsJsonArray();
				  String[] messageparam =null;
				  messageparam=new String[billingMessageparamArray.size()];
				  
				  for(int i=0; i<billingMessageparamArray.size();i++){
					  
					  messageparam[i] =billingMessageparamArray.get(i).toString();
				       
				  }
				  if(messageparam.length>0){
				  Long number= new Long(1);
				  ArrayList<String> addparamdata=new ArrayList<String>();
					 for (String messageparamdetails : messageparam)
					 { 
				     final JsonElement element = fromApiJsonHelper.parse(messageparamdetails);
					 final String parameterName = fromApiJsonHelper.extractStringNamed("parameter", element);
					 addparamdata.add(parameterName);
					 Long sequenceNo=number;
					 BillingMessageParam billingMessageParam = new BillingMessageParam(sequenceNo, parameterName);
					 messageTemplate.setBillingMessageParam(billingMessageParam);
					 this.billingMessageParamRepository.save(billingMessageParam);
					 number++;
			          }					 
				     }//if
				   
				     this.billingMessageTemplateRepository.save(messageTemplate);
				     
	                 return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(messageTemplate.getId()).build();
			  
			         } catch (DataIntegrityViolationException dve) {
	                            return CommandProcessingResult.empty();
	                    }
			
		       }
		
		@SuppressWarnings("null")
		@Override
		public CommandProcessingResult deleteMessageTemplate(JsonCommand command) {
			// TODO Auto-generated method stub
			context.authenticatedUser();
			final BillingMessageTemplate messageTemplate=retriveMessageBy(command.entityId());
			if (messageTemplate == null) {
				throw new DataIntegrityViolationException(messageTemplate.toString());
			}
			messageTemplate.isDelete();
		    this.billingMessageTemplateRepository.save(messageTemplate);
			//this.billingMessageTemplateRepository.delete(messageTemplate);
			return new CommandProcessingResultBuilder().withEntityId(command.entityId()).build();
		}
		
		
		private BillingMessageTemplate retriveMessageBy(Long MessageId) {
			final BillingMessageTemplate billingMessageTemplate = this.billingMessageTemplateRepository.findOne(MessageId);
			return billingMessageTemplate;
		}

}
