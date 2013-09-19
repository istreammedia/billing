package org.mifosplatform.billing.adjustment.serializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;
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
public class AdjustmentCommandFromApiJsonDeserializer {

	/**
	 * The parameters supported for this command.
	 */
	private final Set<String> supportedParameters = new HashSet<String>(
			Arrays.asList("adjustment_date", "adjustment_code", "adjustment_type",
				    "amount_paid", "bill_id", "external_id", "Remarks", "locale",
				    "dateFormat"));
	
	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	public AdjustmentCommandFromApiJsonDeserializer(
			final FromJsonHelper fromApiJsonHelper) {
		this.fromApiJsonHelper = fromApiJsonHelper;
	}

	public void validateForCreate(String json) {

		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}

		final Type typeOfMap = new TypeToken<Map<String, Object>>() {
		}.getType();
		fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json,
				supportedParameters);

		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(
				dataValidationErrors).resource("adjustment");

		final JsonElement element = fromApiJsonHelper.parse(json);
		
		final LocalDate adjustment_date = fromApiJsonHelper.extractLocalDateNamed("adjustment_date", element);
		final String adjustment_code = fromApiJsonHelper.extractStringNamed("adjustment_code", element);
		final BigDecimal amount_paid = fromApiJsonHelper.extractBigDecimalWithLocaleNamed("amount_paid", element);
	//	final String remarks = fromApiJsonHelper.extractStringNamed("Remarks", element);
		final String adjustment_type = fromApiJsonHelper.extractStringNamed("adjustment_type", element);

	
		baseDataValidator.reset().parameter("adjustment_date").value(adjustment_date)
		.notBlank().notExceedingLengthOf(100);
		baseDataValidator.reset().parameter("adjustment_code").value(adjustment_code)
		.notBlank().notExceedingLengthOf(100);
		baseDataValidator.reset().parameter("amount_paid").value(amount_paid)
		.notBlank().notExceedingLengthOf(100);
		baseDataValidator.reset().parameter("adjustment_type").value(adjustment_type)
		.notBlank().notExceedingLengthOf(100);
		//baseDataValidator.reset().parameter("Remarks").value(remarks)
		//.notBlank().notExceedingLengthOf(100);
		

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

