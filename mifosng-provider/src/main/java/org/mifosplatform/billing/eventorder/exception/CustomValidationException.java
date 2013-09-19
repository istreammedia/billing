package org.mifosplatform.billing.eventorder.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;


public class CustomValidationException extends AbstractPlatformDomainRuleException {

    public CustomValidationException() {
        super("error.msg.client.plan.details.not.found", "Client has no Prepaid Plans ");
    }
    
   
}
