package org.mifosplatform.billing.batchjob.serialization;

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

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;

@Component
public class BatchJobFromApiJsonDeserializer {

	private final Set<String> supportedParams = new HashSet<String>(Arrays.asList("batchName","query"));
	private final FromJsonHelper fromJsonHelper;  
	
	@Autowired
	public BatchJobFromApiJsonDeserializer(final FromJsonHelper fromJsonHelper) {
		this.fromJsonHelper = fromJsonHelper;
	}
	
	public void validateForCreate(String json){
		
		if(StringUtils.isBlank(json)){
			throw new InvalidJsonException();
		}
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType(); 
		fromJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParams);
		
		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseValidatorBuilder = new DataValidatorBuilder(dataValidationErrors);
		
		final JsonElement element = fromJsonHelper.parse(json);
		
		final String batchName = fromJsonHelper.extractStringNamed("batchName", element);
		baseValidatorBuilder.reset().parameter("batchName").value(batchName).notBlank().notExceedingLengthOf(20);
		
		final String query = fromJsonHelper.extractStringNamed("query", element);
		baseValidatorBuilder.reset().parameter("query").value(query).notBlank();
		
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
		
		
	}

	private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
}
