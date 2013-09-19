package org.mifosplatform.billing.pricing.exceptions;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

@SuppressWarnings("serial")
public class ChargeCOdeExists extends AbstractPlatformDomainRuleException {

	public ChargeCOdeExists(final String chrgeCode) {
		super("plan.is.already.exists.with.charge.code", "plan is already existed with charge code:"+chrgeCode, chrgeCode);
	}

}
