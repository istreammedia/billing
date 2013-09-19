package org.mifosplatform.billing.media.exceptions;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;


public class NoPricesFoundException extends AbstractPlatformDomainRuleException {

    public NoPricesFoundException() {
        super("error.msg.movie.price.not.found", " No Price Found for This Client Type");
    }
    
   
}
