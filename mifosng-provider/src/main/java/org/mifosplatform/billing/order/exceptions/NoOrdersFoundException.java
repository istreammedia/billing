package org.mifosplatform.billing.order.exceptions;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;


public class NoOrdersFoundException extends AbstractPlatformDomainRuleException {

    public NoOrdersFoundException() {
        super("error.msg.billing.order.not.found", "order not found ");
    }
    
    public NoOrdersFoundException(String msg) {
        super("error.msg.no.bills.to.generate", " No Bills TO Generate ", msg);
    }

	public NoOrdersFoundException(Long orderId) {
		 super("error.msg.orders.are.does.not.exist", "No Orders are does not exist with this "+orderId,orderId);
		 
	}
}
