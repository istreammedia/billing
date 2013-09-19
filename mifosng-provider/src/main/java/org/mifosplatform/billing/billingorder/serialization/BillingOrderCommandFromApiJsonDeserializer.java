package org.mifosplatform.billing.billingorder.serialization;

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
@Component
public class BillingOrderCommandFromApiJsonDeserializer {

	private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("dateFormat","locale","systemDate"));
	
	private final FromJsonHelper fromApiJsonHelper;
	
	@Autowired
	BillingOrderCommandFromApiJsonDeserializer(final FromJsonHelper formApiJsonHelper){
		this.fromApiJsonHelper = formApiJsonHelper;
		
	}
	
	  public void validateForCreate(final String json) {
		  if(StringUtils.isBlank(json)){
			  throw new InvalidJsonException();
		  }
		  
		   final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	       fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

	        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("invoice");
	        final JsonElement element = fromApiJsonHelper.parse(json);
	        
	        
	        final String locale = fromApiJsonHelper.extractStringNamed("locale", element);
	        final String dataFormat = fromApiJsonHelper.extractStringNamed("dateFormat", element);
	        final LocalDate systemDate = fromApiJsonHelper.extractLocalDateNamed("systemDate", element);
	        
	        
	        
	        baseDataValidator.reset().parameter("locale").value(locale).notBlank();
	        baseDataValidator.reset().parameter("dataFormat").value(dataFormat).notNull();
	        baseDataValidator.reset().parameter("systemDate").value(systemDate).notBlank();
			
			
			
	        
			throwExceptionIfValidationWarningsExist(dataValidationErrors);
	  }
	
	  private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
	        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
	                "Validation errors exist.", dataValidationErrors); }
	    }
}

