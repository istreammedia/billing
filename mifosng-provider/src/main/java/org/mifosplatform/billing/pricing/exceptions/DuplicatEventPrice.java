package org.mifosplatform.billing.pricing.exceptions;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

@SuppressWarnings("serial")
public class DuplicatEventPrice extends AbstractPlatformDomainRuleException {

	public DuplicatEventPrice(final String formatType) {
		super("event.is.already.exists.with.format", "plan is already existed with format:"+formatType, formatType);
	}

}
