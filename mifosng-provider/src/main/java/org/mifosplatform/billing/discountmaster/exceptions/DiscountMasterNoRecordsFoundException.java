package org.mifosplatform.billing.discountmaster.exceptions;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;


public class  DiscountMasterNoRecordsFoundException extends AbstractPlatformDomainRuleException {

    public DiscountMasterNoRecordsFoundException() {
        super("error.msg.discount.not.found", "No discount found");
    }
    
  
}
