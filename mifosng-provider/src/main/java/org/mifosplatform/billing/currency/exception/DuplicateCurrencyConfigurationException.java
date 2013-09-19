package org.mifosplatform.billing.currency.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

@SuppressWarnings("serial")
public class DuplicateCurrencyConfigurationException extends AbstractPlatformDomainRuleException {

	public DuplicateCurrencyConfigurationException(final String country) {
		super("currency.is.already.configured.with.this.country", "Currency is already cinfigured with "+country,country);
	}

}
