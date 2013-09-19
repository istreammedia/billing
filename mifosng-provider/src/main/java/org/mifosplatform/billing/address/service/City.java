package org.mifosplatform.billing.address.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;


@Entity
@Table(name="b_city",uniqueConstraints = @UniqueConstraint(name = "city_code", columnNames = { "city_code" }))
public class City extends AbstractAuditableCustom<AppUser, Long> {


@Column(name="city_code")
private String cityCode;

@Column(name="city_name")
private String cityName;

@Column(name="parent_code")
private Long parentCode;
public City(String entityCode, String entityName, Long parentEntityId) {

	 this.cityCode=entityCode;
	 this.cityName=entityName;
	 this.parentCode=parentEntityId;


	}
public static City fromJson(JsonCommand command) {
	 final String cityCode = command.stringValueOfParameterNamed("entityCode");
	    final String cityName = command.stringValueOfParameterNamed("entityName");
	    final Long parentEntityId = command.longValueOfParameterNamed("parentEntityId");
	     return new City(cityCode,cityName, parentEntityId);
}

}
