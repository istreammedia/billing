/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.billing.region.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

/**
 * A {@link RuntimeException} thrown when a code is not found.
 */
public class RegionNotFoundException extends AbstractPlatformResourceNotFoundException {

    public RegionNotFoundException(final String name) {
        super("error.msg.region.not.found", "Region with id`" + name + "` does not exist", name);
    }

    public RegionNotFoundException(final Long codeId) {
        super("error.msg.region.not.found", "Region with identifier `" + codeId + "` does not exist", codeId);
    }
}