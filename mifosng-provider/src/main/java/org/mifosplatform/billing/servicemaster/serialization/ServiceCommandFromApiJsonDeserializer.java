package org.mifosplatform.billing.servicemaster.serialization;

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

/**
 * Deserializer for code JSON to validate API request.
 */
@Component
public final class ServiceCommandFromApiJsonDeserializer {

    /**
     * The parameters supported for this command.
     */
    private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("serviceCode","serviceDescription","serviceType",
    		"serviceUnitType","serviceType","status","isOptional"));
    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public ServiceCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    public void validateForCreate(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("service");

        final JsonElement element = fromApiJsonHelper.parse(json);

        final String serviceCode = fromApiJsonHelper.extractStringNamed("serviceCode", element);
        baseDataValidator.reset().parameter("serviceCode").value(serviceCode).notBlank().notExceedingLengthOf(100);
        final String serviceDescription = fromApiJsonHelper.extractStringNamed("serviceDescription", element);
        baseDataValidator.reset().parameter("serviceDescription").value(serviceDescription).notBlank();
        final String  serviceType = fromApiJsonHelper.extractStringNamed("serviceType", element);
        baseDataValidator.reset().parameter("serviceType").value(serviceType).notBlank().notExceedingLengthOf(100);
        /*final String  serviceUnitType = fromApiJsonHelper.extractStringNamed("serviceUnitType", element);
        baseDataValidator.reset().parameter("serviceUnitType").value(serviceUnitType).notBlank();*/
        final String  status = fromApiJsonHelper.extractStringNamed("status", element);
        baseDataValidator.reset().parameter("status").value(status).notBlank();
       

        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    public void validateForUpdate(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("code");

        final JsonElement element = fromApiJsonHelper.parse(json);
        if (fromApiJsonHelper.parameterExists("name", element)) {
            final String name = fromApiJsonHelper.extractStringNamed("name", element);
            baseDataValidator.reset().parameter("name").value(name).notBlank().notExceedingLengthOf(100);
        }

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
}