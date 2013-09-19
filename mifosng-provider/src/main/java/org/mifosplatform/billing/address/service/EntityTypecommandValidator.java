package org.mifosplatform.billing.address.service;

import java.util.ArrayList;
import java.util.List;

import org.mifosplatform.billing.address.data.EntityTypecommand;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;


public class EntityTypecommandValidator {

	private final EntityTypecommand command;

	public EntityTypecommandValidator(final EntityTypecommand command) {
		this.command=command;
	}


	public void validateForCreate() {
         List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("address");
		baseDataValidator.reset().parameter("entityCode").value(command.getEntityCode()).integerGreaterThanZero().notNull().notBlank();
		baseDataValidator.reset().parameter("entityName").value(command.getEntityName()).notBlank().notNull();

		if (!dataValidationErrors.isEmpty()) {
			throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.", dataValidationErrors);
		}
	}
}
