package org.mifosplatform.billing.selfcare.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
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
public class SelfCareCommandFromApiJsonDeserializer {

private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("clientId","userName","password","uniqueReference","isDeleted"));

    
    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public SelfCareCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    public void validateForCreate(final JsonCommand command) {
        if (StringUtils.isBlank(command.toString())) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, command.json(),supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("selfcare");


        final JsonElement element = fromApiJsonHelper.parse(command.json());
        
        if(fromApiJsonHelper.parameterExists("userName", element)){
        	final String userName = fromApiJsonHelper.extractStringNamed("userName", element); 
        	baseDataValidator.reset().parameter("userName").value(userName).notNull().notExceedingLengthOf(100);
        }
        
        
        
        if(fromApiJsonHelper.parameterExists("uniqueReference", element)){
        	final String uniqueReference = fromApiJsonHelper.extractStringNamed("uniqueReference", element);
        	baseDataValidator.reset().parameter("uniqueReference").value(uniqueReference).notBlank();
        }
        
		

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
}
