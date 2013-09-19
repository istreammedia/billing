/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.infrastructure.core.domain;

public class MifosPlatformTenant {

    private final Long id;
    private final String name;
    private final String schemaName;
    private final String schemaServer;
    private final String schemaServerPort;
    private final String schemaUsername;
    private final String schemaPassword;
    private final String timezoneId;
    private final boolean autoUpdateEnabled;
	private final String tenantIdentifier;

    public MifosPlatformTenant(final Long id, final String name, final String schemaName, final String schemaServer,
            final String schemaServerPort, final String schemaUsername, final String schemaPassword, String timezoneId,
            final boolean autoUpdateEnabled, String identifier) {
        this.id = id;
        this.name = name;
        this.schemaName = schemaName;
        this.schemaServer = schemaServer;
        this.schemaServerPort = schemaServerPort;
        this.schemaUsername = schemaUsername;
        this.schemaPassword = schemaPassword;
        this.timezoneId = timezoneId;
        this.autoUpdateEnabled = autoUpdateEnabled;
        this.tenantIdentifier=identifier;

    }

    public String databaseURL() {
        String url = new StringBuilder("jdbc:mysql://").append(schemaServer).append(':').append(schemaServerPort).append('/')
                .append(schemaName).toString();
        return url;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSchemaName() {
        return this.schemaName;
    }

    public String getSchemaUsername() {
        return schemaUsername;
    }

    public String getSchemaPassword() {
        return schemaPassword;
    }

    public String getTimezoneId() {
        return timezoneId;
    }

    public boolean isAutoUpdateEnabled() {
        return this.autoUpdateEnabled;
    }

	public String getTenantIdentifier() {
		 return this.tenantIdentifier;
	}

}