package org.mifosplatform.billing.ticketmaster.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.InvalidJsonException;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Component
public class TicketMasterFromApiJsonDeserializer {
	
	
	private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("priority","problemCode","description","assignedTo","ticketDate","ticketTime","locale","dateFormat"));
	private final FromJsonHelper fromApiJsonHelper;
	
	@Autowired
	public TicketMasterFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
		this.fromApiJsonHelper = fromApiJsonHelper;
	}

	 public void validateForCreate(final String json) {
	        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }
	        
	        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);
	        
	        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("ticketmaster");
	        
	        final JsonElement element = fromApiJsonHelper.parse(json);
	        
	        final String priority = fromApiJsonHelper.extractStringNamed("priority", element);
	        final String problemCode = fromApiJsonHelper.extractStringNamed("problemCode", element);
	        final String description = fromApiJsonHelper.extractStringNamed("description", element);
	        final String assignedTo = fromApiJsonHelper.extractStringNamed("assignedTo", element);
	        
	        baseDataValidator.reset().parameter("priority").value(priority).notBlank().notExceedingLengthOf(100);
	        baseDataValidator.reset().parameter("problemCode").value(problemCode).notBlank().notExceedingLengthOf(100);
	        baseDataValidator.reset().parameter("description").value(description).notBlank().notExceedingLengthOf(100);
	        baseDataValidator.reset().parameter("assignedTo").value(assignedTo).notBlank().notExceedingLengthOf(100);
	        
	        throwExceptionIfValidationWarningsExist(dataValidationErrors);
	 }
	 
	 private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
	        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
	                "Validation errors exist.", dataValidationErrors); }
	    }
	
}
