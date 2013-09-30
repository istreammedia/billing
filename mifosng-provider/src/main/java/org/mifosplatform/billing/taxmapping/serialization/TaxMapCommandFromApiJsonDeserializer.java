package org.mifosplatform.billing.taxmapping.serialization;

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
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.InvalidJsonException;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;


/**
 * Deserializer for code JSON to validate API request.
 */

@Component
public final class TaxMapCommandFromApiJsonDeserializer {

	  /**
     * The parameters supported for this command.
     */
	
	private final Set<String> supportedParams = new HashSet<String>(Arrays.asList("chargeCode","taxCode","startDate","type","rate","locale","dateFormat","taxRegion"));
	
	private final FromJsonHelper fromJsonHelper;
	
	@Autowired
	public TaxMapCommandFromApiJsonDeserializer(final FromJsonHelper fromJsonHelper) {
		this.fromJsonHelper = fromJsonHelper;
	}
	
	public void validateForCreate(final JsonCommand command){
		
		if(StringUtils.isBlank(command.toString())){
			throw new InvalidJsonException();
		}
		
		final Type typeOfMap = new TypeToken<Map<String,Object>>(){}.getType();
		fromJsonHelper.checkForUnsupportedParameters(typeOfMap, command.json(), supportedParams);
		
		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("taxmap");
		
		
		final String taxCode = command.stringValueOfParameterNamed("taxCode");
		final LocalDate startDate = command.localDateValueOfParameterNamed("startDate");
		final String type = command.stringValueOfParameterNamed("type");
		final BigDecimal rate = command.bigDecimalValueOfParameterNamed("rate"); 

		baseDataValidator.reset().parameter("taxCode").value(taxCode).notBlank().notExceedingLengthOf(10);
		baseDataValidator.reset().parameter("startDate").value(startDate).notBlank();
		if(type.contains("-1"))
			baseDataValidator.reset().parameter("type").value(type).notBlank().notExceedingLengthOf(15).zeroOrPositiveAmount();
		else
			baseDataValidator.reset().parameter("type").value(type).notBlank().notExceedingLengthOf(15);
		
		baseDataValidator.reset().parameter("rate").value(rate).notBlank();
	
		throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}
	
	private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
}
