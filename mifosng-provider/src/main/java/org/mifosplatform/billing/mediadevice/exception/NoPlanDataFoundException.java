package org.mifosplatform.billing.mediadevice.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class NoPlanDataFoundException extends AbstractPlatformDomainRuleException {

    public NoPlanDataFoundException() {
        super("error.msg.device.details.not.found", "Device Details are does not exist ");
    }
    
   
}
