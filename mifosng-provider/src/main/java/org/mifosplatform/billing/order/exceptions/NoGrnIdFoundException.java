package org.mifosplatform.billing.order.exceptions;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;


public class NoGrnIdFoundException extends AbstractPlatformDomainRuleException {

    

	public NoGrnIdFoundException(Long orderId) {
		 super("error.msg.order.quantity..exceeds", "No more order quantity"+orderId,orderId);
		 
	}
}
