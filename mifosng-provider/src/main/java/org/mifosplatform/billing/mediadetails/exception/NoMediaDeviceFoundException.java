package org.mifosplatform.billing.mediadetails.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;


public class NoMediaDeviceFoundException extends AbstractPlatformDomainRuleException {

    public NoMediaDeviceFoundException() {
        super("error.msg.device.details.not.found", "Device Details are does not exist ");
    }
    
   
}
