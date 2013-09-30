package org.mifosplatform.billing.inventory.mrn.serialization;

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
public class MRNDetailsCommandFromApiJsonDeserializer {

	private FromJsonHelper fromJsonHelper;
	private final Set<String> supportedParams = new HashSet<String>(Arrays.asList("requestedDate","requestedTime","itemDescription","fromOffice","toOffice","orderdQuantity","status","locale","dateFormat","mrnId","serialNumber","movedDate"));
	@Autowired
	public MRNDetailsCommandFromApiJsonDeserializer(final FromJsonHelper fromJsonHelper) {
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
		
		
		final Integer fO = fromJsonHelper.extractIntegerWithLocaleNamed("fromOffice", element);
		Long fromOffice = null;
		if(fO!=null){
			fromOffice = fO.longValue();
		}
		
		final Integer tO = fromJsonHelper.extractIntegerWithLocaleNamed("toOffice", element);		
		Long toOffice = null;
		if(tO!=null){
			toOffice = tO.longValue();
		}
		
		final Integer iD = fromJsonHelper.extractIntegerWithLocaleNamed("itemDescription", element);
		Long itemDescription = null;
		if(iD!=null){
			itemDescription = iD.longValue();
		}
		
		//final Integer oQ = fromJsonHelper.extractIntegerWithLocaleNamed("orderdQuantity", element);
		final BigDecimal orderdQuantity = fromJsonHelper.extractBigDecimalNamed("orderdQuantity", element, fromJsonHelper.extractLocaleParameter(element.getAsJsonObject()));
		final LocalDate requestedDate = fromJsonHelper.extractLocalDateNamed("requestedDate", element); 
		
		baseValidatorBuilder.reset().parameter("fromOffice").value(fromOffice).notBlank().notExceedingLengthOf(10);
		baseValidatorBuilder.reset().parameter("toOffice").value(toOffice).notBlank().notExceedingLengthOf(10);
		baseValidatorBuilder.reset().parameter("orderdQuantity").value(orderdQuantity).notBlank().notExceedingLengthOf(10).positiveAmount();
		baseValidatorBuilder.reset().parameter("itemDescription").value(itemDescription).notBlank().notExceedingLengthOf(10);
		baseValidatorBuilder.reset().parameter("fromOffice").value(fromOffice).notBlank().notSameAsParameter("toOffice", toOffice);
		
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}
	
public void validateForMove(String json){
		
		if(StringUtils.isBlank(json)){
			throw new InvalidJsonException();
		}
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType(); 
		fromJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParams);
		
		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseValidatorBuilder = new DataValidatorBuilder(dataValidationErrors);
		
		final JsonElement element = fromJsonHelper.parse(json);
		
		
		final Integer md = fromJsonHelper.extractIntegerWithLocaleNamed("mrnId", element);
		
			baseValidatorBuilder.reset().parameter("mrnId").value(md).notNull();
		
		final String serialNumber = fromJsonHelper.extractStringNamed("serialNumber", element);
		baseValidatorBuilder.reset().parameter("serialNumber").value(serialNumber).notBlank().notExceedingLengthOf(100);
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}
	private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
	
}
