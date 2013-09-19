package org.mifosplatform.billing.onetimesale.serialization;

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
public final class OneTimesaleCommandFromApiJsonDeserializer {

    /**
     * The parameters supported for this command.
     */
    private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("itemId","locale","dateFormat","units","chargeCode","unitPrice",
    		"quantity","totalPrice","saleDate","discountId"));
    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public OneTimesaleCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    public void validateForCreate(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("onetimesale");

        final JsonElement element = fromApiJsonHelper.parse(json);

        final Long itemId = fromApiJsonHelper.extractLongNamed("itemId", element);
        baseDataValidator.reset().parameter("itemId").value(itemId).notNull();
        final Long discountId = fromApiJsonHelper.extractLongNamed("discountId", element);
        baseDataValidator.reset().parameter("discountId").value(discountId).notNull();
        final String chargeCode = fromApiJsonHelper.extractStringNamed("chargeCode", element);
        baseDataValidator.reset().parameter("chargeCode").value(chargeCode).notBlank();
        final BigDecimal unitPrice=fromApiJsonHelper.extractBigDecimalWithLocaleNamed("unitPrice", element);
        baseDataValidator.reset().parameter("unitPrice").value(unitPrice).notNull();
        final BigDecimal totalPrice=fromApiJsonHelper.extractBigDecimalWithLocaleNamed("totalPrice", element);
        baseDataValidator.reset().parameter("totalPrice").value(totalPrice).notNull();
        final String quantity = fromApiJsonHelper.extractStringNamed("quantity", element);
        baseDataValidator.reset().parameter("quantity").value(quantity).notBlank().notExceedingLengthOf(50);
        final LocalDate saleDate = fromApiJsonHelper.extractLocalDateNamed("saleDate", element);
        baseDataValidator.reset().parameter("saleDate").value(saleDate).notBlank();
        

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }  public void validateForPrice(final JsonElement jsonElement) {
        if (StringUtils.isBlank(jsonElement.toString())) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, jsonElement.toString(), supportedParameters);
       // final JsonElement element = fromApiJsonHelper.parse(jsonElement);
        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("onetimesale");
        final Integer totalPrice=fromApiJsonHelper.extractIntegerWithLocaleNamed("quantity", jsonElement);
        baseDataValidator.reset().parameter("unitPrice").value(totalPrice).notNull().integerGreaterThanZero();
        

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }
   
    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
}