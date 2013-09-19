/**
 * 
 */
package org.mifosplatform.billing.eventmaster.domain;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

/**
 * {@link EnumOptionData} for Opt Type
 * 
 * @author pavani
 *
 */
public enum OptType {

	RENT(1,"Category.Rent"),
	OWN(2,"Category.own"),
	INVALID(3,"category.invalid");
	
	
	private final Integer value;
	private final String code;
	
	private OptType(final Integer value, final String code) {
		this.value = value;
		this.code = code;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	public static OptType fromInt(final Integer frequency) {
		
		OptType optType = OptType.INVALID;
		switch(frequency) {
		case 1:
			optType = OptType.RENT;
			break;
		case 2:
			optType = OptType.OWN;
			break;
		default:
			optType = OptType.INVALID;
			break;
		}
		return optType;
	}
}
