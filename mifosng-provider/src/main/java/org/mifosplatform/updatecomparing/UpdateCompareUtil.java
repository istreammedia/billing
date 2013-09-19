package org.mifosplatform.updatecomparing;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.mifosplatform.annotation.ComparableFields;

/**
 * Util class for Mifos events.
 * @author pavani
 *
 */
public class UpdateCompareUtil {

	
	/**
	 * Compare the values of two entity for getting changed values.
	 * It returns {@link Object} containing old and new values in 
	 *  
	 * @param oldValueEntity - Object entity with old values
	 * @param newValueEntity - Object entity with new values
	 * @return EventLog - Containing changed old value and new value
	 */
	public static Object compare(Object oldValueEntity, Object newValueEntity){
		
		if(oldValueEntity.getClass().getName().equals(newValueEntity.getClass().getName())){
			Annotation[] annotations = oldValueEntity.getClass().getAnnotations();
			for(Annotation annotation : annotations){
			    if(annotation instanceof ComparableFields){
			    	ComparableFields comparableFields = (ComparableFields) annotation;
			        String[] fields = comparableFields.on();
			        for(String field : fields){
			        	try {
							Object oldValue = PropertyUtils.getProperty(oldValueEntity, field);
							Object newValue = PropertyUtils.getProperty(newValueEntity, field);
							if(oldValue != null){
				        		if(!oldValue.equals(newValue)){
				        			PropertyUtils.setProperty(oldValueEntity, field, newValue);
				        		}
				        	} else if(newValue != null){
				        	}
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
			        }
			    }
			}
		}
		return oldValueEntity;
	}
}
