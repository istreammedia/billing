package org.mifosplatform.billing.plan.exceptions;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

public class PlanNotFundException extends AbstractPlatformResourceNotFoundException {

public PlanNotFundException() {
super("error.msg.depositproduct.id.invalid",
		"Charge Code already exists with same plan");
}

}
