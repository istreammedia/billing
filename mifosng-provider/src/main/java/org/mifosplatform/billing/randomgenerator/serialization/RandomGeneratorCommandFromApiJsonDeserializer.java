package org.mifosplatform.billing.randomgenerator.serialization;

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
public class RandomGeneratorCommandFromApiJsonDeserializer {

	/**
	 * The parameters supported for this command.
	 */
	private final Set<String> supportedParameters = new HashSet<String>(
			Arrays.asList("id", "batchName", "batchDescription", "length",
					"beginWith", "pinCategory", "pinType", "quantity",
					"serialNo", "expiryDate", "dateFormat", "pinValue",
					"pinNO", "locale", "pinExtention"));
	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	public RandomGeneratorCommandFromApiJsonDeserializer(
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
				dataValidationErrors).resource("randomGenerator");

		final JsonElement element = fromApiJsonHelper.parse(json);

		final String batchName = fromApiJsonHelper.extractStringNamed(
				"batchName", element);
		baseDataValidator.reset().parameter("batchName").value(batchName)
				.notBlank();

		final String batchDescription = fromApiJsonHelper.extractStringNamed(
				"batchDescription", element);
		baseDataValidator.reset().parameter("batchDescription")
				.value(batchDescription).notBlank();
		
		final BigDecimal length1 = fromApiJsonHelper.extractBigDecimalWithLocaleNamed("length", element);
	    baseDataValidator.reset().parameter("length").value(length1).notNull();

		final String pinCategory = fromApiJsonHelper.extractStringNamed(
				"pinCategory", element);
		baseDataValidator.reset().parameter("pinCategory").value(pinCategory)
				.notBlank();

		final String beginWith = fromApiJsonHelper.extractStringNamed(
				"beginWith", element);
		baseDataValidator.reset().parameter("beginWith").value(beginWith)
				.notBlank();

		final String pinType = fromApiJsonHelper.extractStringNamed("pinType",
				element);
		baseDataValidator.reset().parameter("pinType").value(pinType)
				.notBlank();

		final BigDecimal Quantity1 = fromApiJsonHelper.extractBigDecimalWithLocaleNamed("quantity", element);
	    baseDataValidator.reset().parameter("quantity").value(Quantity1).notNull();
	    
		final BigDecimal Serial1 = fromApiJsonHelper.extractBigDecimalWithLocaleNamed("serialNo", element);
	    baseDataValidator.reset().parameter("serialNo").value(Serial1).notNull();
	    
	    final BigDecimal pinValue1 = fromApiJsonHelper.extractBigDecimalWithLocaleNamed("pinValue", element);
	    baseDataValidator.reset().parameter("pinValue").value(pinValue1).notNull();

		final String pinExtention = fromApiJsonHelper.extractStringNamed(
				"pinExtention", element);
		baseDataValidator.reset().parameter("pinExtention").value(pinExtention)
				.notBlank();

		final LocalDate ExpiryDate = fromApiJsonHelper.extractLocalDateNamed(
				"expiryDate", element);
		baseDataValidator.reset().parameter("expiryDate").value(ExpiryDate)
				.notBlank();
		
		if (!(Serial1 == null || Quantity1 == null || length1 == null
				|| pinValue1 == null || pinExtention == null)) {

			if (!(Serial1.longValue() < 0 || Quantity1.longValue() < 0 || length1.longValue() < 0 || pinValue1.longValue() < 0)) {

				String minSerialSeries = "";
				String maxSerialSeries = "";
				for (int x = 0; x < Serial1.longValue(); x++) {
					if (x == 0) {
						minSerialSeries += "1";
						maxSerialSeries += "9";
					} else {
						maxSerialSeries += "9";
					}
				}

				int minNo = Integer.parseInt(minSerialSeries);
				int maxNo = Integer.parseInt(maxSerialSeries);
				baseDataValidator.reset().parameter("quantity").value(Quantity1.longValue())
						.inMinMaxRange(minNo, maxNo);
				int val = beginWith.length();
				baseDataValidator.reset().parameter("beginWith").value(val)
						.notGreaterThanMax(length1);
				baseDataValidator.reset().parameter("pinValue").value(pinValue1.longValue())
						.inMinMaxRange(1, 1000000000);
			}
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
