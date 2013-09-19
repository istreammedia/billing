package org.mifosplatform.billing.onetimesale.serialization;

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
public final class EventOrderCommandFromApiJsonDeserializer {

    /**
     * The parameters supported for this command.
     */
    private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("clientId","locale","dateFormat","eventId","eventBookedDate",
    		"formatType","optType","deviceId"));
    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public EventOrderCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    public void validateForCreate(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("eventorder");

        final JsonElement element = fromApiJsonHelper.parse(json);

        final String deviceId = fromApiJsonHelper.extractStringNamed("deviceId", element);
        baseDataValidator.reset().parameter("deviceId").value(deviceId).notNull().notBlank();
        final Long eventId = fromApiJsonHelper.extractLongNamed("eventId", element);
        baseDataValidator.reset().parameter("eventId").value(eventId).notBlank();
        final String formatType = fromApiJsonHelper.extractStringNamed("formatType", element);
        baseDataValidator.reset().parameter("formatType").value(formatType).notBlank();
        final String optType = fromApiJsonHelper.extractStringNamed("optType", element);
        baseDataValidator.reset().parameter("optType").value(optType).notBlank();
        final LocalDate eventBookedDate = fromApiJsonHelper.extractLocalDateNamed("eventBookedDate", element);
        baseDataValidator.reset().parameter("eventBookedDate").value(eventBookedDate).notBlank();
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }  
   
    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
}