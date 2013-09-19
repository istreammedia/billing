package org.mifosplatform.billing.address.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.data.jpa.domain.AbstractAuditable;


@Entity
@Table(name="b_state",uniqueConstraints=@UniqueConstraint( name="state_code",columnNames ={ "state_code" }))
public class State extends AbstractAuditable<AppUser,Long>{

@Column(name="state_code")
private String stateCode;

@Column(name="state_name")
private String stateName;

@Column(name ="parent_code")
private Long parentCode;


public State(String entityCode, String entityName, Long parentEntityId) {
	  this.stateCode=entityCode;
	  this.stateName=entityName;
	  this.parentCode=parentEntityId;


	}


public static State fromJson(JsonCommand command) {
	 final String cityCode = command.stringValueOfParameterNamed("entityCode");
	    final String cityName = command.stringValueOfParameterNamed("entityName");
	    final Long parentEntityId = command.longValueOfParameterNamed("parentEntityId");
	     return new State(cityCode,cityName, parentEntityId);
}

}
