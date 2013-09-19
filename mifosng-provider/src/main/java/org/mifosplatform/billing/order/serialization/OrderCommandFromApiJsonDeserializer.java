package org.mifosplatform.billing.order.serialization;

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
public final class OrderCommandFromApiJsonDeserializer {

    /**
     * The parameters supported for this command.
     */
    private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("planCode","locale","dateFormat","start_date","paytermCode","contractPeriod",
    		"billAlign","price","description","renewalPeriod","disconnectReason"));
    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public OrderCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    public void validateForCreate(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("order");

        final JsonElement element = fromApiJsonHelper.parse(json);

        final String planCode = fromApiJsonHelper.extractStringNamed("planCode", element);
        baseDataValidator.reset().parameter("planCode").value(planCode).notBlank();
        final LocalDate startDate = fromApiJsonHelper.extractLocalDateNamed("start_date", element);
        baseDataValidator.reset().parameter("start_date").value(startDate).notBlank();
        final String paytermCode = fromApiJsonHelper.extractStringNamed("paytermCode", element);
        baseDataValidator.reset().parameter("paytermCode").value(paytermCode).notBlank();

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    public void validateForUpdate(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("code");

        final JsonElement element = fromApiJsonHelper.parse(json);
        if (fromApiJsonHelper.parameterExists("renewalPeriod", element)) {
            final String renewalPeriod = fromApiJsonHelper.extractStringNamed("renewalPeriod", element);
            baseDataValidator.reset().parameter("renewalPeriod").value(renewalPeriod).notBlank();
        }

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }

	public void validateForRenewalOrder(String json) {
		   if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

	        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

	        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("order");

	        final JsonElement element = fromApiJsonHelper.parse(json);

	        final String renewalPeriod = fromApiJsonHelper.extractStringNamed("renewalPeriod", element);
	        baseDataValidator.reset().parameter("renewalPeriod").value(renewalPeriod).notBlank();
	        throwExceptionIfValidationWarningsExist(dataValidationErrors);
		
	}

	public void validateForDisconnectOrder(String json) {
		 if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

	        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

	        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("order");

	        final JsonElement element = fromApiJsonHelper.parse(json);

	        final String disconnectReason = fromApiJsonHelper.extractStringNamed("disconnectReason", element);
	        baseDataValidator.reset().parameter("disconnectReason").value(disconnectReason).notBlank();
	        throwExceptionIfValidationWarningsExist(dataValidationErrors);
		
	}
}