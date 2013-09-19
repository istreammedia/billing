package org.mifosplatform.billing.address.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class AddressNoRecordsFoundException extends AbstractPlatformDomainRuleException {

	public AddressNoRecordsFoundException() {
		super("error.msg.billing.address.city.not.found", "City Not Found");
		// TODO Auto-generated constructor stub
	}

}
