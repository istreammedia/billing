package org.mifosplatform.billing.chargecode.serialization;

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

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;

@Component
public class ChargeCodeCommandFromApiJsonDeserializer {

	final private Set<String> supportedParameters = new HashSet<String>(Arrays.asList("billFrequencyCode","chargeCode","chargeDescription","chargeDuration","chargeType","dateFormat","durationType","locale","taxInclusive"));
	private final FromJsonHelper fromApiJsonHelper;  
	
	@Autowired
	public ChargeCodeCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper){
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	public void validaForCreate(String json){
		if(StringUtils.isBlank(json)){
			throw new InvalidJsonException();
		}
		
		
		 final Type typeOfMap = new TypeToken<Map<String, Object>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;}.getType();
			
		 fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);
		 
		 final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	     final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("chargecode");

	     final JsonElement element = fromApiJsonHelper.parse(json);

	     final String billFrequencyCode = fromApiJsonHelper.extractStringNamed("billFrequencyCode", element);
	     if(billFrequencyCode.contains("-1"))
	    	 baseDataValidator.reset().parameter("billFrequencyCode").value(billFrequencyCode).notBlank().zeroOrPositiveAmount();
	     else
	    	 baseDataValidator.reset().parameter("billFrequencyCode").value(billFrequencyCode).notBlank();
	     
	     final String chargeCode = fromApiJsonHelper.extractStringNamed("chargeCode", element);
	     baseDataValidator.reset().parameter("chargeCode").value(chargeCode).notBlank();
	     
	     final String chargeDescription = fromApiJsonHelper.extractStringNamed("chargeDescription", element);
	     baseDataValidator.reset().parameter("chargeDescription").value(chargeDescription).notBlank();
	     
	     final Long chargeDuration = fromApiJsonHelper.extractLongNamed("chargeDuration", element);
	     baseDataValidator.reset().parameter("chargeDuration").value(chargeDuration).notBlank().notExceedingLengthOf(2);
	     
	     
	     final String chargeType = fromApiJsonHelper.extractStringNamed("chargeType", element);
	     if(chargeType.contains("-1"))
	    	 baseDataValidator.reset().parameter("chargeType").value(chargeType).notBlank().zeroOrPositiveAmount();
	     else
	    	 baseDataValidator.reset().parameter("chargeType").value(chargeType).notBlank();
	     
	     final String durationType = fromApiJsonHelper.extractStringNamed("durationType", element);
	     if(durationType.contains("-1"))
	    	 baseDataValidator.reset().parameter("durationType").value(durationType).notBlank().zeroOrPositiveAmount();
	     else
	    	 baseDataValidator.reset().parameter("durationType").value(durationType).notBlank();
	     
	     final boolean taxInclusive = fromApiJsonHelper.extractBooleanNamed("taxInclusive", element);
	     baseDataValidator.reset().parameter("taxInclusive").value(taxInclusive).notBlank();
	   
	     
	     throwExceptionIfValidationWarningsExist(dataValidationErrors);

	}
	
	private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
}
