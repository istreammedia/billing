package org.mifosplatform.billing.batchjobscheduling.serialization;

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
public class BatchJobSchedulingCommandFromApiJsonDeserializer {

	private FromJsonHelper fromJsonHelper;
	private final Set<String> supportedParams = new HashSet<String>(Arrays.asList("batchName","scheduleType","dateFormat","process","status","msgTemplateDescription","scheduleTime","locale"));
	@Autowired
	public BatchJobSchedulingCommandFromApiJsonDeserializer(final FromJsonHelper fromJsonHelper) {
		this.fromJsonHelper = fromJsonHelper;
	}
	
	
	public void validateForCreate(String json){
		
		if(StringUtils.isBlank(json)){
			throw new InvalidJsonException();
		}
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType(); 
		fromJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParams);
		
		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseValidatorBuilder = new DataValidatorBuilder(dataValidationErrors);
		
		final JsonElement element = fromJsonHelper.parse(json);
		
		final String batchName = fromJsonHelper.extractStringNamed("batchName", element);
		baseValidatorBuilder.reset().parameter("batchName").value(batchName).notBlank().notExceedingLengthOf(20);
		 if(batchName.contains("-1"))
			 baseValidatorBuilder.reset().parameter("batchName").value(batchName).notBlank().zeroOrPositiveAmount();
	     else
	    	 baseValidatorBuilder.reset().parameter("batchName").value(batchName).notBlank();
	     
		
		final String scheduleType = fromJsonHelper.extractStringNamed("scheduleType", element);
		baseValidatorBuilder.reset().parameter("scheduleType").value(scheduleType).notBlank().notExceedingLengthOf(20);
		 if(scheduleType.contains("-1"))
			 baseValidatorBuilder.reset().parameter("scheduleType").value(scheduleType).zeroOrPositiveAmount();
	     else
	    	 baseValidatorBuilder.reset().parameter("scheduleType").value(scheduleType).notBlank();
		
		if(scheduleType.equalsIgnoreCase("Recurring")){
			baseValidatorBuilder.reset().parameter("scheduleType").value(scheduleType).isOneOfTheseValues("Once");
		}
		
		final String process = fromJsonHelper.extractStringNamed("process", element);
		 if(process.contains("-1"))
			 baseValidatorBuilder.reset().parameter("process").value(process).notBlank().zeroOrPositiveAmount();
	     else
	    	 baseValidatorBuilder.reset().parameter("process").value(process).notBlank().notExceedingLengthOf(20);
		
		final String status = fromJsonHelper.extractStringNamed("status", element);
		baseValidatorBuilder.reset().parameter("status").value(status).notBlank().notExceedingLengthOf(1);
		
		if(process.equalsIgnoreCase("Message")){
			final String msgTemplateDescription = fromJsonHelper.extractStringNamed("msgTemplateDescription", element);
			
			if(msgTemplateDescription.contains("-1"))
				 baseValidatorBuilder.reset().parameter("msgTemplateDescription").value(msgTemplateDescription).notBlank().zeroOrPositiveAmount();
		     else
		    	 baseValidatorBuilder.reset().parameter("msgTemplateDescription").value(msgTemplateDescription).notBlank();
			
			baseValidatorBuilder.reset().parameter("msgTemplateDescription").value(msgTemplateDescription).notBlank();
			System.out.println(msgTemplateDescription);
		}
		
		final String scheduleTime = fromJsonHelper.extractStringNamed("scheduleTime", element);
		baseValidatorBuilder.reset().parameter("scheduleTime").value(scheduleTime).notBlank();
		System.out.println(scheduleTime);
		
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}
	private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
	
}
