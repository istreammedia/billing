package org.mifosplatform.billing.plan.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
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
public final class PlanCommandFromApiJsonDeserializer {

    /**
     * The parameters supported for this command.
     */
    private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("planCode","planDescription","locale",
    		"dateFormat","startDate","endDate","status","chargeCode","roles","billRule",
    		"provisioingSystem","services","duration","volume","isPrepaid","units","allowTopup"));
    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public PlanCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    public void validateForCreate(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("plan");

        final JsonElement element = fromApiJsonHelper.parse(json);

        final String planCode = fromApiJsonHelper.extractStringNamed("planCode", element);
        baseDataValidator.reset().parameter("planCode").value(planCode).notBlank().notExceedingLengthOf(100);
        final LocalDate startDate = fromApiJsonHelper.extractLocalDateNamed("startDate", element);
        baseDataValidator.reset().parameter("startDate").value(startDate).notBlank();
        final String[] services = fromApiJsonHelper.extractArrayNamed("services", element);
        baseDataValidator.reset().parameter("services").value(services).arrayNotEmpty();
        final String provisioingSystem = fromApiJsonHelper.extractStringNamed("provisioingSystem", element);
        baseDataValidator.reset().parameter("provisioingSystem").value(provisioingSystem).notBlank();
        final Long status = fromApiJsonHelper.extractLongNamed("status", element);
        baseDataValidator.reset().parameter("status").value(status).notNull();
        final boolean isPrepaid=fromApiJsonHelper.extractBooleanNamed("isPrepaid", element);
        if(isPrepaid){
        	final String duration=fromApiJsonHelper.extractStringNamed("duration", element);
        	baseDataValidator.reset().parameter("duration").value(duration).notBlank();
        	final String volumeType=fromApiJsonHelper.extractStringNamed("volume", element);
        	baseDataValidator.reset().parameter("volume").value(volumeType).notNull();
        	final String units=fromApiJsonHelper.extractStringNamed("units", element);
        	 baseDataValidator.reset().parameter("units").value(units).notBlank();
        }
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