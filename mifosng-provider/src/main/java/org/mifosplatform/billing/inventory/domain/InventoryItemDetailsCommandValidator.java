package org.mifosplatform.billing.inventory.domain;

import java.util.ArrayList;
import java.util.List;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;

/**
 * @author rahman
 *
 */
public class InventoryItemDetailsCommandValidator {

	
	private final JsonCommand command;
	
	
	public JsonCommand getCommand() {
		return command;
	}

	public InventoryItemDetailsCommandValidator(final JsonCommand command) {
		this.command=command;
	}
	
	
	

	/**
	 * 
	 */
	public void validateForCreate(){
			List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
			DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("ItemDetails");
			//baseDataValidator.reset().parameter("serviceCode").value(command.getServiceCode()).notBlank().notNull();
			baseDataValidator.reset().parameter("itemMasterId").value(command.integerValueOfParameterNamed("itemMasterId")).integerGreaterThanZero().notBlank().notNull().validateforNumeric();
			baseDataValidator.reset().parameter("serialNumber").value(command.stringValueOfParameterNamed("serialNumber")).notBlank().notNull();
			baseDataValidator.reset().parameter("grnId").value(command.longValueOfParameterNamed("grnId")).longGreaterThanZero().notBlank().notNull();
			baseDataValidator.reset().parameter("provisioningSerialNumber").value(command.stringValueOfParameterNamed("provisioningSerialNumber")).notBlank().notNull();
			baseDataValidator.reset().parameter("quality").value(command.stringValueOfParameterNamed("quality")).notBlank().notNull();
			baseDataValidator.reset().parameter("status").value(command.stringValueOfParameterNamed("status")).notBlank().notNull(); 	
			baseDataValidator.reset().parameter("warranty").value(command.integerValueOfParameterNamed("warranty")).integerGreaterThanZero().notNull().notBlank().validateforNumeric();
			baseDataValidator.reset().parameter("remarks").value(command.stringValueOfParameterNamed("remarks")).notBlank().notNull();
			if (!dataValidationErrors.isEmpty()) {
				throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.", dataValidationErrors);
				//throw new InventoryItemDetailsExist(command.getSerialNumber());
			}
		}
			
			
			
	}


