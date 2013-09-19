package org.mifosplatform.billing.ticketmaster.service;

import java.util.ArrayList;
import java.util.List;

import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.billing.ticketmaster.command.TicketMasterCommand;



public class TicketMasterCommandValidator {

	

		private final TicketMasterCommand command;

		public TicketMasterCommandValidator(final TicketMasterCommand
				command) {
			this.command=command;
		}


		public void validateForCreate() {
	         List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
			DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("ticketmaster");
			baseDataValidator.reset().parameter("priority").value(command.getPriority()).notBlank();
			//baseDataValidator.reset().parameter("subscription_type").value(command.getSubscription_type()).notBlank().notNull();
			baseDataValidator.reset().parameter("problemCode").value(command.getProblemCode()).notBlank();
			baseDataValidator.reset().parameter("description").value(command.getDescription()).notBlank();
		//	baseDataValidator.reset().parameter("status").value(command.getStatus()).notBlank();
		//	baseDataValidator.reset().parameter("resolutionDescription").value(command.getResolutionDescription()).notBlank();
		    baseDataValidator.reset().parameter("assignedTo").value(command.getAssignedTo()).notBlank();

			if (!dataValidationErrors.isEmpty()) {
				throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.", dataValidationErrors);
			}
		}
	}


