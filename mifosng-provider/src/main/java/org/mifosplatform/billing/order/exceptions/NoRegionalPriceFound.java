package org.mifosplatform.billing.order.exceptions;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;


public class NoRegionalPriceFound extends AbstractPlatformDomainRuleException {

    public NoRegionalPriceFound() {
        super("error.msg.regional.price.not.found", " No Regional Price Available for this Plan");
    }
    
   
}
