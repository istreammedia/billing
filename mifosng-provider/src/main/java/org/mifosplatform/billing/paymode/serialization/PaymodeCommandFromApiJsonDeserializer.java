package org.mifosplatform.billing.paymode.serialization;

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
public class PaymodeCommandFromApiJsonDeserializer {

	/**
	 * The parameters supported for this command.
	 */
	private final Set<String> supportedParameters = new HashSet<String>(
			Arrays.asList("id", "code_id", "code_value", "order_position"));
	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	public PaymodeCommandFromApiJsonDeserializer(
			final FromJsonHelper fromApiJsonHelper) {
		this.fromApiJsonHelper = fromApiJsonHelper;
	}

	public void validateForCreate(final String json) {
		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}

		final Type typeOfMap = new TypeToken<Map<String, Object>>() {
		}.getType();
		fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json,
				supportedParameters);

		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(
				dataValidationErrors).resource("paymodes");

		final JsonElement element = fromApiJsonHelper.parse(json);
	
		final String codeId = fromApiJsonHelper.extractStringNamed("code_id", element);
	        baseDataValidator.reset().parameter("code_id").value(codeId).notBlank();
	        
	    final String codeValue = fromApiJsonHelper.extractStringNamed("code_value", element);
	        baseDataValidator.reset().parameter("code_value").value(codeValue).notBlank();
	        
		final Long order_position = fromApiJsonHelper.extractLongNamed("order_position", element);
          baseDataValidator.reset().parameter("order_position").value(order_position).notNull();
        
		throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}

	public void validateForUpdate(final String json) {
		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}

		final Type typeOfMap = new TypeToken<Map<String, Object>>() {
		}.getType();
		fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json,
				supportedParameters);

		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(
				dataValidationErrors).resource("paymodes");

		final JsonElement element = fromApiJsonHelper.parse(json);

		if (fromApiJsonHelper.parameterExists("code_id", element)) {
			final String codeId = fromApiJsonHelper.extractStringNamed("code_id", element);
			baseDataValidator.reset().parameter("code_id").value(codeId)
					.notBlank().notExceedingLengthOf(100);
		}
		if (fromApiJsonHelper.parameterExists("code_value", element)) {
			final String codeValue = fromApiJsonHelper.extractStringNamed("code_value", element);
			baseDataValidator.reset().parameter("code_value").value(codeValue).notBlank().notExceedingLengthOf(100);
		}
		if (fromApiJsonHelper.parameterExists("order_position", element)) {
			final Long orderPosition = fromApiJsonHelper.extractLongNamed("order_position", element);
			baseDataValidator.reset().parameter("order_position")
					.value(orderPosition).notBlank().notExceedingLengthOf(100).integerGreaterThanZero();
		}
		throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}

	private void throwExceptionIfValidationWarningsExist(
			final List<ApiParameterError> dataValidationErrors) {
		if (!dataValidationErrors.isEmpty()) {
			throw new PlatformApiDataValidationException(
					"validation.msg.validation.errors.exist",
					"Validation errors exist.", dataValidationErrors);
		}
	}
}
