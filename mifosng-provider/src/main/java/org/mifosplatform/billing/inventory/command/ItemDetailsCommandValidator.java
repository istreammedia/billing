package org.mifosplatform.billing.inventory.command;

import java.util.ArrayList;
import java.util.List;

import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.billing.inventory.command.ItemDetailsCommand;

public class ItemDetailsCommandValidator {

	
	private final ItemDetailsCommand command;
	
	
	public ItemDetailsCommandValidator(final ItemDetailsCommand command) {
		this.command=command;
	}
	
/*	
	private String itemMasterId;
	private String serialNumber;
	private String grnId;
	private String provisioningSerialNumber;
	private String quality;
	private String status;
	private Long officeId;
	private Long clientId;
	private Long warranty;
	private String remark;
	private final Set<String> modifiedParameters;*/	
	public void validateForCreate(){
			List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
			DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("Item Details");
			//baseDataValidator.reset().parameter("serviceCode").value(command.getServiceCode()).notBlank().notNull();
			baseDataValidator.reset().parameter("itemMasterId").value(command.getItemMasterId()).notNull();
			baseDataValidator.reset().parameter("serialNumber").value(command.getSerialNumber()).notNull();
			baseDataValidator.reset().parameter("grnId").value(command.getGrnId()).notNull();
			baseDataValidator.reset().parameter("provisioningSerialNumber").value(command.getProvisioningSerialNumber()).notNull();
			baseDataValidator.reset().parameter("quality").value(command.getQuality()).notNull();
			baseDataValidator.reset().parameter("status").value(command.getStatus()).notNull();
			baseDataValidator.reset().parameter("officeId").value(command.getOfficeId()).notNull();
			baseDataValidator.reset().parameter("clientId").value(command.getClass()).notNull();
			baseDataValidator.reset().parameter("warranty").value(command.getWarranty()).notNull();
			baseDataValidator.reset().parameter("remark").value(command.getRemarks()).notNull();
			
			if (!dataValidationErrors.isEmpty()) {
				throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.", dataValidationErrors);
			}
		}
			
			
			
	}


