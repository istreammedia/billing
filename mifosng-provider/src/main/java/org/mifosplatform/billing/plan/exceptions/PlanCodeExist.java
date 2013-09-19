package org.mifosplatform.billing.plan.exceptions;
import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

@SuppressWarnings("serial")
public class PlanCodeExist extends AbstractPlatformDomainRuleException {

		public PlanCodeExist(final String planCode) {
			super("service.is.already.exists.with.plan.code", "service is already existed with plan code:"+planCode, planCode);
		}

	}