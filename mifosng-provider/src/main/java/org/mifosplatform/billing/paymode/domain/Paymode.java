package org.mifosplatform.billing.paymode.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@Entity
@Table(name = "m_code_value")
public class Paymode extends AbstractPersistable<Long> {

	
	@Column(name = "code_id", length = 100)
	private int codeId;
	
	@Column(name = "code_value", nullable = false)
	private String codeValue;

	@Column(name = "order_position", length = 100)
	private int orderPosition;
	

	public Paymode() {
		// zero-param constructor
	}

	public Paymode(int codeId, String codeValue, int orderPosition) {
		this.codeId = codeId;
		this.codeValue = codeValue;
        this.orderPosition=orderPosition;
	}

	public static Paymode fromJson(JsonCommand command, Long id) {
		
		final Long codeId = id;
		final String codeValue = command.stringValueOfParameterNamed("code_value");
		final Long orderPosition = command.longValueOfParameterNamed("order_position");
		
		return new Paymode(codeId.intValue(), codeValue, orderPosition.intValue());

	}

	public Map<String, Object> update(JsonCommand command, Long id) {
		
		final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(
				1);

		//this.codeId = id.intValue();
		
		final String codeValue = "code_value";
		if (command.isChangeInStringParameterNamed(codeValue,
				this.codeValue)) {
			final String newValue = command
					.stringValueOfParameterNamed("code_value");
			actualChanges.put(codeValue, newValue);
			this.codeValue = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String orderPosition = "order_position";
		if (command.isChangeInLongParameterNamed(orderPosition,
				Long.valueOf(this.orderPosition))) {
			final Long newValue = command
					.longValueOfParameterNamed("order_position");
			actualChanges.put(orderPosition, newValue);
			this.orderPosition = newValue.intValue();
		}

		return actualChanges;

	}


	

	
}
