/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.infrastructure.codes.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

/**
 * A {@link RuntimeException} thrown when a code is not found.
 */
public class DiscountNotFoundException extends AbstractPlatformResourceNotFoundException {

    public DiscountNotFoundException(final String discountId) {
        super("error.msg.discount.not.found", "Discount with this id"+discountId+"not exist",discountId);
        
    }

   
}