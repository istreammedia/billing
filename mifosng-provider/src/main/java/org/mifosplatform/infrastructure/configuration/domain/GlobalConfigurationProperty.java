/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.infrastructure.configuration.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "c_configuration")
public class GlobalConfigurationProperty extends AbstractPersistable<Long> {

    @SuppressWarnings("unused")
    @Column(name = "name", nullable = false)
    private final String name;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "value", nullable = false)
    private final String value;
    
    protected GlobalConfigurationProperty() {
        this.name = null;
        this.enabled = false;
        this.value=null;
    }

    public GlobalConfigurationProperty(final String name, final boolean enabled,final String value) {
        this.name = name;
        this.enabled = enabled;
        this.value=value;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean updateTo(final boolean value) {
        final boolean updated = this.enabled != value;
        this.enabled = value;
        return updated;
    }

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
    
    
}