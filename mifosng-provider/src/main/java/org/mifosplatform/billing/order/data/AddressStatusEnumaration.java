package org.mifosplatform.billing.order.data;



import org.mifosplatform.billing.address.domain.AddressEnum;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class AddressStatusEnumaration {

	public static EnumOptionData enumOptionData(final int id) {
		return enumOptionData(AddressEnum.fromInt(id));
	}

	public static EnumOptionData enumOptionData(final AddressEnum addressEnum) {
		final String codePrefix = "deposit.interest.compounding.period.";
		EnumOptionData optionData = null;
		switch (addressEnum) {
		case PRIMARY:
			optionData = new EnumOptionData(AddressEnum.PRIMARY.getValue().longValue(), codePrefix + AddressEnum.PRIMARY.getCode(), "PRIMARY");
			break;
		case BILLING:
			optionData = new EnumOptionData(AddressEnum.BILLING.getValue().longValue(), codePrefix + AddressEnum.BILLING.getCode(), "BILLING");
			break;
		default:
			optionData = new EnumOptionData(AddressEnum.INVALID.getValue().longValue(), AddressEnum.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
