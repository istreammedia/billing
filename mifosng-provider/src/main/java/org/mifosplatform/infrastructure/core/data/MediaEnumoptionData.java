/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.infrastructure.core.data;

/**
 * <p>
 * Immutable data object representing generic enumeration value.
 * </p>
 */
@SuppressWarnings("unused")
public class MediaEnumoptionData {

    private final String id;
    private final String code;
    private final String value;
    public MediaEnumoptionData(final String id, final String code, final String value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    public String getId() {
        return id;
    }

	public String getValue() {
		return value;
	}
}