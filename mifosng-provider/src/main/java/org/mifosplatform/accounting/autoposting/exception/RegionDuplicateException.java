/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.accounting.autoposting.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

/**
 * A {@link RuntimeException} thrown when a Auto posting rule is already present
 */
public class RegionDuplicateException extends AbstractPlatformDomainRuleException {

    public RegionDuplicateException(final String regionCodeName) {
        super("error.msg.region.duplicate", "An Region with the  " + regionCodeName + " already exists",
        		regionCodeName);
    }
    
    public RegionDuplicateException(final Long countryID) {
        super("error.msg.country.region.duplicate", "An Region with the  " + countryID+ " already exists",
        		countryID);
    }

	}