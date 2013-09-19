package org.mifosplatform.billing.uploadstatus.domain;

import java.util.ArrayList;
import java.util.List;

import org.mifosplatform.billing.uploadstatus.command.UploadStatusCommand;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;

public class UploadStatusCommandValidator {

	
	private final UploadStatusCommand command;
	
	
	public UploadStatusCommandValidator(final UploadStatusCommand command) {
		this.command=command;
	}
	
/*	
	private String uploadProcess;
	private String uploadFilePath;
	private Date processDate;
	private String processStatus;
	private Long processRecords;
	private String errorMessage;
	private char isDeleted;
	private final Set<String> modifiedParameters;
	*/	
	public void validateForCreate(){
			List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
			DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("UploadStatus");
			//baseDataValidator.reset().parameter("serviceCode").value(command.getServiceCode()).notBlank().notNull();
			//baseDataValidator.reset().parameter("uploadProcess").value(command.getUploadProcess()).notBlank().notNull();
			baseDataValidator.reset().parameter("fileName").value(command.getFileName()).notNull();
			//baseDataValidator.reset().parameter("processDate").value(command.getProcessDate()).notBlank().notNull();
			//baseDataValidator.reset().parameter("processStatus").value(command.getProcessStatus()).notBlank().notNull();
			//baseDataValidator.reset().parameter("processRecords").value(command.getProcessRecords()).notBlank().notNull();
			//baseDataValidator.reset().parameter("errorMessage").value(command.getErrorMessage()).notBlank().notNull();
			baseDataValidator.reset().parameter("description").value(command.getDescription()).notNull();
			
			
			
			if (!dataValidationErrors.isEmpty()) {
				throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.", dataValidationErrors);
			}
		}
	
			
			
	}


