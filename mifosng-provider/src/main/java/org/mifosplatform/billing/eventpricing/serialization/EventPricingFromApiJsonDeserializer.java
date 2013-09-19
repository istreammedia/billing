/**
 * 
 */
package org.mifosplatform.billing.eventpricing.serialization;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.billing.eventpricing.domain.EventPricing;
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
 * {@link Component} CLass for Validating Json for {@link EventPricing}
 * 
 * @author pavani
 *
 */
@Component
public class EventPricingFromApiJsonDeserializer {

	private final FromJsonHelper fromApiJsonHelper;
	private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("clientType","discountId","eventId","formatType","locale","optType","price"));
	
	@Autowired
	public EventPricingFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	public void validateForCreate(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }
        
        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("eventPricing");
        
        final JsonElement element = fromApiJsonHelper.parse(json);
        
        final BigDecimal price = fromApiJsonHelper.extractBigDecimalWithLocaleNamed("price", element);
        baseDataValidator.reset().parameter("priority").value(price).notBlank().notExceedingLengthOf(100);

        final Long clientType=fromApiJsonHelper.extractLongNamed("clientType", element);
        baseDataValidator.reset().parameter("clientType").value(clientType).notNull();
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
 }
 
 private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    } 
	
}
