package org.mifosplatform.billing.inventory.serialization;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
public class InventoryGrnCommandFromApiJsonDeserializer {

	private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("supplierId","itemMasterId","orderdQuantity","officeId","purchaseDate","grnlisttable_length","locale","dateFormat","balanceQuantity","itemDescription"));
	
	private final FromJsonHelper fromApiJsonHelper;
	
	@Autowired
	InventoryGrnCommandFromApiJsonDeserializer(final FromJsonHelper formApiJsonHelper){
		this.fromApiJsonHelper = formApiJsonHelper;
		
	}
	
	  public void validateForCreate(final String json) {
		  if(StringUtils.isBlank(json)){
			  throw new InvalidJsonException();
		  }
		  
		   final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	       fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

	        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("grn-allocation");
	        final JsonElement element = fromApiJsonHelper.parse(json);
	        
	        
	        final Long supplierId = fromApiJsonHelper.extractLongNamed("supplierId", element);
	        final Long officeId = fromApiJsonHelper.extractLongNamed("officeId", element);
	        //final Long orderdQuantity = fromApiJsonHelper.extractLongNamed("orderdQuantity", element);
	        final BigDecimal orderdQuantity = fromApiJsonHelper.extractBigDecimalNamed("orderdQuantity", element, fromApiJsonHelper.extractLocaleParameter(element.getAsJsonObject()));
	        final Long itemMasterId = fromApiJsonHelper.extractLongNamed("itemMasterId", element);
	        
	        final LocalDate purchaseDate = fromApiJsonHelper.extractLocalDateNamed("purchaseDate", element);
	        
	        
	        
			baseDataValidator.reset().parameter("supplierId").value(supplierId).notNull();
			baseDataValidator.reset().parameter("orderdQuantity").value(orderdQuantity).notBlank().positiveAmount();
			baseDataValidator.reset().parameter("itemMasterId").value(itemMasterId).notBlank();
			
			baseDataValidator.reset().parameter("officeId").value(officeId).notBlank();
			baseDataValidator.reset().parameter("purchaseDate").value(purchaseDate).notBlank();
			
	        
			throwExceptionIfValidationWarningsExist(dataValidationErrors);
	  }
	
	  private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
	        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
	                "Validation errors exist.", dataValidationErrors); }
	    }
}
