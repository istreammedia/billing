package org.mifosplatform.billing.clientprospect.serialization;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
public class ClientProspectCommandFromApiJsonDeserializer {

	
	private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("prospectType","firstName","middleName","lastName","homePhoneNumber","workPhoneNumber","mobileNumber","email","sourceOfPublicity","preferredCallingTime","note","address","streetArea","cityDistrict","state","country","locale","preferredPlan","status","statusRemark","callStatus","assignedTo","notes","isDeleted","zipCode"));
	
	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	ClientProspectCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper){
		this.fromApiJsonHelper = fromApiJsonHelper;
		
	}
	
	 public void validateForCreate(final String json) {
		  if(StringUtils.isBlank(json)){
			  throw new InvalidJsonException();
		  }
		  
		   final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	       fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

	        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("client.prospect");
	        final JsonElement element = fromApiJsonHelper.parse(json);
	        
	        if(fromApiJsonHelper.parameterExists("type", element)){
	        	baseDataValidator.reset().parameter("type").value(fromApiJsonHelper.extractBooleanNamed("type", element)).notBlank();
	        }
	        
	        if(fromApiJsonHelper.parameterExists("firstName", element)){
	        	baseDataValidator.reset().parameter("firstName").value(fromApiJsonHelper.extractStringNamed("firstName", element)).notBlank();
	        }
	        if(fromApiJsonHelper.parameterExists("lastName", element)){
	        	baseDataValidator.reset().parameter("lastName").value(fromApiJsonHelper.extractStringNamed("lastName", element)).notBlank();
	        }
	        
	        if(fromApiJsonHelper.parameterExists("address", element)){
	        	baseDataValidator.reset().parameter("address").value(fromApiJsonHelper.extractStringNamed("address", element)).notBlank();
	        }
	        
	        if(fromApiJsonHelper.parameterExists("streetArea", element)){
	        	baseDataValidator.reset().parameter("streetArea").value(fromApiJsonHelper.extractStringNamed("streetArea", element)).notBlank();
	        }
	        if(fromApiJsonHelper.parameterExists("cityDistrict", element)){
	        	baseDataValidator.reset().parameter("cityDistrict").value(fromApiJsonHelper.extractStringNamed("cityDistrict", element)).notBlank();
	        }
	        if(fromApiJsonHelper.parameterExists("state", element)){
	        	baseDataValidator.reset().parameter("state").value(fromApiJsonHelper.extractStringNamed("state", element)).notBlank();
	        }
	        if(fromApiJsonHelper.parameterExists("country", element)){
	        	baseDataValidator.reset().parameter("country").value(fromApiJsonHelper.extractStringNamed("country", element)).notBlank();
	        }
	        if(fromApiJsonHelper.parameterExists("zipCode", element)){
	        	baseDataValidator.reset().parameter("zipCode").value(fromApiJsonHelper.extractStringNamed("zipCode", element)).notBlank().validateForZip(fromApiJsonHelper.extractStringNamed("zipCode", element));
	        }
	        if(fromApiJsonHelper.parameterExists("note", element)){
	        	baseDataValidator.reset().parameter("note").value(fromApiJsonHelper.extractStringNamed("note", element)).notBlank().notExceedingLengthOf(255);
	        }
	        
	        if(fromApiJsonHelper.parameterExists("email", element)){
	        	baseDataValidator.reset().parameter("email").value(fromApiJsonHelper.extractStringNamed("email", element)).notBlank().validateEmailExpresstion(fromApiJsonHelper.extractStringNamed("email", element));
	        }
	        
	        if(fromApiJsonHelper.parameterExists("mobileNumber", element)){
	        	baseDataValidator.reset().parameter("mobileNumber").value(fromApiJsonHelper.extractStringNamed("mobileNumber", element)).notBlank().validateMobileNumber(fromApiJsonHelper.extractStringNamed("mobileNumber", element));
	        }
	        
	        if(fromApiJsonHelper.parameterExists("homePhoneNumber", element)){
	        	baseDataValidator.reset().parameter("homePhoneNumber").value(fromApiJsonHelper.extractStringNamed("homePhoneNumber", element)).notBlank().validateLandLineNumber(fromApiJsonHelper.extractStringNamed("homePhoneNumber", element));
	        }
	        if(fromApiJsonHelper.parameterExists("workPhoneNumber", element)){
	        	baseDataValidator.reset().parameter("workPhoneNumber").value(fromApiJsonHelper.extractStringNamed("workPhoneNumber", element)).notBlank().validateLandLineNumber(fromApiJsonHelper.extractStringNamed("workPhoneNumber", element));
	        }
	        
	        
	        if(fromApiJsonHelper.parameterExists("preferredCallingTime", element)){
				String startDateString = fromApiJsonHelper.extractStringNamed("preferredCallingTime", element);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date preferredCallingTime  = null;
				try {
					preferredCallingTime = df.parse(startDateString);
				} catch (Exception e) {
					baseDataValidator.reset().parameter("preferredCallingTime").value(preferredCallingTime).notBlank();	
				}
				
			}
	        
			
			
	        
			throwExceptionIfValidationWarningsExist(dataValidationErrors);
	  }
	 public void validateForUpdate(final String json) {
		  if(StringUtils.isBlank(json)){
			  throw new InvalidJsonException();
		  }
		  
		   final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	       fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

	        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("client.prospect");
	        final JsonElement element = fromApiJsonHelper.parse(json);
	        
	        if(fromApiJsonHelper.parameterExists("type", element)){
	        	baseDataValidator.reset().parameter("type").value(fromApiJsonHelper.extractBooleanNamed("type", element)).notBlank();
	        }
	        
	        
	        
	        
	        
	       final Long assignedTo = fromApiJsonHelper.extractLongNamed("assignedTo", element);
	        final Long callStatus = fromApiJsonHelper.extractLongNamed("callStatus", element);
	        final String notes = fromApiJsonHelper.extractStringNamed("notes", element);
	        final String preferredCallingTime = fromApiJsonHelper.extractStringNamed("preferredCallingTime", element);
	        
	        
	        
	        baseDataValidator.reset().parameter("assignedTo").value(assignedTo).notBlank();
	        baseDataValidator.reset().parameter("callStatus").value(callStatus).notNull();
	        baseDataValidator.reset().parameter("notes").value(notes).notBlank();
	        baseDataValidator.reset().parameter("preferredCallingTime").value(preferredCallingTime).notBlank();
			
		
			
			
	        
			throwExceptionIfValidationWarningsExist(dataValidationErrors);
	  }
	
	  private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
	        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
	                "Validation errors exist.", dataValidationErrors); }
	  }	
}
