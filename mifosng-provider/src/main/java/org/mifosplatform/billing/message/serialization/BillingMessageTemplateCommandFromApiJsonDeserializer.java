package org.mifosplatform.billing.message.serialization;

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
public class BillingMessageTemplateCommandFromApiJsonDeserializer {
	
	/**
	 * The parameters supported for this command.
	 */
	
	  private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("templateDescription","subject","locale",
	    		"query","header","body","footer","msgTemplateId","parameterName","sequenceNo","messageParams"));
	  
	    private final FromJsonHelper fromApiJsonHelper;
	    
	    @Autowired
	    public BillingMessageTemplateCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
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
					dataValidationErrors).resource("message");

			final JsonElement element = fromApiJsonHelper.parse(json);
		
			final String templateDescription = fromApiJsonHelper.extractStringNamed("templateDescription", element);
			final String subject = fromApiJsonHelper.extractStringNamed("subject", element);
			final String header = fromApiJsonHelper.extractStringNamed("header", element);
			final String body = fromApiJsonHelper.extractStringNamed("body", element);
			final String footer = fromApiJsonHelper.extractStringNamed("footer", element);

			baseDataValidator.reset().parameter("templateDescription").value(templateDescription)
			.notBlank().notExceedingLengthOf(256);
			baseDataValidator.reset().parameter("subject").value(subject)
			.notBlank().notExceedingLengthOf(256);
			baseDataValidator.reset().parameter("header").value(header)
			.notBlank().notExceedingLengthOf(256);
			baseDataValidator.reset().parameter("body").value(body)
			.notBlank().notExceedingLengthOf(2560);
			baseDataValidator.reset().parameter("footer").value(footer)
			.notBlank().notExceedingLengthOf(256);
			

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
