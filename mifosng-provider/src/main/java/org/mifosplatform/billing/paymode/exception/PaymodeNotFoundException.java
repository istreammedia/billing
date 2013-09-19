package org.mifosplatform.billing.paymode.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

/**
 * A {@link RuntimeException} thrown when a code is not found.
 */

public class PaymodeNotFoundException extends
		AbstractPlatformResourceNotFoundException {

	public PaymodeNotFoundException(final String name) {
		super("error.msg.paymode.not.found", "Paymode with name `" + name
				+ "` does not exist", name);
	}

	public PaymodeNotFoundException(final Long codeId) {
		super("error.msg.Paymode.identifier.not.found",
				"Paymode with identifier `" + codeId + "` does not exist",
				codeId);
	}
}
