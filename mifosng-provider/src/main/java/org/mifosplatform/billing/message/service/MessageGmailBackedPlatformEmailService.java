/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.billing.message.service;

import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.mifosplatform.billing.message.data.BillingMessageDataForProcessing;
import org.mifosplatform.billing.message.domain.BillingMessage;
import org.mifosplatform.billing.message.domain.MessageDataRepository;
import org.mifosplatform.infrastructure.core.domain.EmailDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageGmailBackedPlatformEmailService implements MessagePlatformEmailService {
	
	private final BillingMesssageReadPlatformService billingMesssageReadPlatformService;
    private final MessageDataRepository messageDataRepository;
	
	 @Autowired
	    public MessageGmailBackedPlatformEmailService(BillingMesssageReadPlatformService billingMesssageReadPlatformService,
	    		MessageDataRepository messageDataRepository){
		
		 this.billingMesssageReadPlatformService=billingMesssageReadPlatformService;
		 this.messageDataRepository=messageDataRepository;
		 
	 }

    @Override
    public void sendToUserEmail() {
        Email email = new SimpleEmail();

        String authuserName = "info@hugotechnologies.com";
        //String authusername="hugotechnologies";

        String authuser = "ashokcse556@gmail.com";
        String authpwd = "9989720715";

        // Very Important, Don't use email.setAuthentication()
        email.setAuthenticator(new DefaultAuthenticator(authuser, authpwd));
        email.setDebug(true); // true if you want to debug
        email.setHostName("smtp.gmail.com");
        try {
            email.getMailSession().getProperties().put("mail.smtp.starttls.enable", "true");
            email.setFrom(authuserName, authuser);
            List<BillingMessageDataForProcessing> billingMessageDataForProcessings=this.billingMesssageReadPlatformService.retrieveMessageDataForProcessing();
      	    for(BillingMessageDataForProcessing emailDetail : billingMessageDataForProcessings){
             
            StringBuilder subjectBuilder = new StringBuilder().append(" ").append(emailDetail.getSubject()).append("  ");

            email.setSubject(subjectBuilder.toString());
     
           String sendToEmail = emailDetail.getMessageTo();

            StringBuilder messageBuilder = new StringBuilder().append(emailDetail.getHeader()).append(".").append(emailDetail.getBody()).append(",").append(emailDetail.getFooter());

            email.setMsg(messageBuilder.toString());

            email.addTo(sendToEmail, emailDetail.getMessageFrom());
            email.setSmtpPort(587);
            email.send();
            BillingMessage billingMessage=this.messageDataRepository.findOne(emailDetail.getId());
            if(billingMessage.getStatus().contentEquals("N"))
            {
            	billingMessage.updateStatus();
            }
            this.messageDataRepository.save(billingMessage);
            
            
           }
      	 } catch (EmailException e) {
            throw new MessagePlatformEmailSendException(e);
        }
    }
}