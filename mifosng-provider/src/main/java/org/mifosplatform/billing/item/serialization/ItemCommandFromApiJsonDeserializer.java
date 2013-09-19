package org.mifosplatform.billing.item.serialization;

import java.lang.reflect.Type;
import java.math.BigDecimal;
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

@Component
public class ItemCommandFromApiJsonDeserializer {
	 
	
	 	Set<String> supportedParameters = new HashSet<String>(Arrays.asList("itemCode","itemDescription","units","chargeCode","locale","unitPrice","warranty","itemClass"));
	    private final FromJsonHelper fromApiJsonHelper;

	    @Autowired
	    public ItemCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
	        this.fromApiJsonHelper = fromApiJsonHelper;
	    }

	    public void validateForCreate(final String json) {
	        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

	        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

	        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
	        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("item");

	        final JsonElement element = fromApiJsonHelper.parse(json);

	        final String itemCode = fromApiJsonHelper.extractStringNamed("itemCode", element);
	        final String itemDescription = fromApiJsonHelper.extractStringNamed("itemDescription", element);
	        final String itemClass = fromApiJsonHelper.extractStringNamed("itemClass", element);
	        final String chargeCode = fromApiJsonHelper.extractStringNamed("chargeCode",element);
	        final String units = fromApiJsonHelper.extractStringNamed("units", element);
	        final Integer w = fromApiJsonHelper.extractIntegerNamed("warranty", element, fromApiJsonHelper.extractLocaleParameter(element.getAsJsonObject()));
	        Long warranty = null;
	        if(w!=null){
	        warranty = w.longValue();
	        }
	        
	        final BigDecimal unitPrice = fromApiJsonHelper.extractBigDecimalNamed("unitPrice", element, fromApiJsonHelper.extractLocaleParameter(element.getAsJsonObject()));
	        
	        baseDataValidator.reset().parameter("chargeCode").value(chargeCode).notNull().notExceedingLengthOf(10);
			baseDataValidator.reset().parameter("itemClass").value(itemClass).notBlank();
			baseDataValidator.reset().parameter("itemCode").value(itemCode).notBlank().notExceedingLengthOf(10);
			baseDataValidator.reset().parameter("itemDescription").value(itemDescription).notBlank();
			baseDataValidator.reset().parameter("unitPrice").value(unitPrice).notNull();
			baseDataValidator.reset().parameter("units").value(units).notBlank();
			baseDataValidator.reset().parameter("warranty").value(warranty).notNull();

	        throwExceptionIfValidationWarningsExist(dataValidationErrors);
	    }
	    
	    
	    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
	        if (!dataValidationErrors.isEmpty()) { 
	        	
	        	throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist","Validation errors exist.",dataValidationErrors); }
	    }

}
