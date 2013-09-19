package org.mifosplatform.billing.message.service;


import java.util.List;

import org.mifosplatform.billing.message.data.BillingMessageData;
import org.mifosplatform.billing.message.serialization.MessageDataCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BillingMessageDataWritePlatformServiceImpl implements BillingMessageDataWritePlatformService {
	
	private final PlatformSecurityContext context;
    private final FromJsonHelper fromApiJsonHelper;
    private final BillingMesssageReadPlatformService billingMesssageReadPlatformService;
    private final MessageDataCommandFromApiJsonDeserializer messageDataCommandFromApiJsonDeserializer;
	
    @Autowired
    public BillingMessageDataWritePlatformServiceImpl(PlatformSecurityContext context,
    	    FromJsonHelper fromApiJsonHelper,BillingMesssageReadPlatformService billingMesssageReadPlatformService,
    		MessageDataCommandFromApiJsonDeserializer messageDataCommandFromApiJsonDeserializer)
    {
    	this.context=context;
    	this.fromApiJsonHelper=fromApiJsonHelper;
    	this.billingMesssageReadPlatformService=billingMesssageReadPlatformService;
    	this.messageDataCommandFromApiJsonDeserializer=messageDataCommandFromApiJsonDeserializer;
    	
    }
    
    
	@Override
	public CommandProcessingResult createMessageData(Long id,String json) {

		//	context.authenticatedUser();
		BillingMessageData templateData=this.billingMesssageReadPlatformService.retrieveMessageTemplate(id);
		List<BillingMessageData> messageparam=this.billingMesssageReadPlatformService.retrieveMessageParams(id);
		List<BillingMessageData> clientData=this.billingMesssageReadPlatformService.retrieveData(id,json,templateData,messageparam);
		
		return new CommandProcessingResultBuilder().withCommandId(id).withEntityId(id).build();

	}

}
