package org.mifosplatform.billing.inventory.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;


public class OrderQuantityExceedsException extends AbstractPlatformDomainRuleException {

    

	public OrderQuantityExceedsException(Long orderId) {
		 super("error.msg.order.quantity..exceeds", "No more order quantity"+orderId,orderId);
		 
	}
}
