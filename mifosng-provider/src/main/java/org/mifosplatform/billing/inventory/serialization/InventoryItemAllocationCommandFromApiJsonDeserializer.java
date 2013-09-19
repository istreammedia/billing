package org.mifosplatform.billing.inventory.serialization;

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
public class InventoryItemAllocationCommandFromApiJsonDeserializer {

	private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("orderId","clientId","itemMasterId","serialNumber","status"));
	
	private final FromJsonHelper fromApiJsonHelper;
	
	@Autowired
	InventoryItemAllocationCommandFromApiJsonDeserializer(final FromJsonHelper formApiJsonHelper){
		this.fromApiJsonHelper = formApiJsonHelper;
		
	}
	
	  public void validateForCreate(final String json) {
		  if(StringUtils.isBlank(json)){
			  throw new InvalidJsonException();
		  }
		  
		   final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	       fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

	        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("item-allocation");
	        final JsonElement element = fromApiJsonHelper.parse(json);
	        
	        
	        final Long orderId = fromApiJsonHelper.extractLongNamed("orderId", element);
	        final Long clientId = fromApiJsonHelper.extractLongNamed("clientId", element);
	        final Long itemMasterId = fromApiJsonHelper.extractLongNamed("itemMasterId", element);
	        final String serialNumber = fromApiJsonHelper.extractStringNamed("serialNumber", element);
	        //final LocalDate allocationDate = fromApiJsonHelper.extra("allocationDate", element);
	        final String status = fromApiJsonHelper.extractStringNamed("status", element);
	        
	        
			baseDataValidator.reset().parameter("orderId").value(orderId).notNull();
			baseDataValidator.reset().parameter("clientId").value(clientId).notNull().notBlank();
			baseDataValidator.reset().parameter("serialNumber").value(serialNumber).notBlank().notNull();
			baseDataValidator.reset().parameter("status").value(status).notNull();
			baseDataValidator.reset().parameter("itemMasterId").value(itemMasterId).notBlank();
			//baseDataValidator.reset().parameter("allocationDate").value(allocationDate).notNull();
	        
			throwExceptionIfValidationWarningsExist(dataValidationErrors);
	  }
	
	  private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
	        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
	                "Validation errors exist.", dataValidationErrors); }
	    }
}
