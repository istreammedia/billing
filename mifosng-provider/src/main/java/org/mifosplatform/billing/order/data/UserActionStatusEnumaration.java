package org.mifosplatform.billing.order.data;



import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.billing.plan.domain.UserActionStatusTypeEnum;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class UserActionStatusEnumaration {

	public static EnumOptionData OrderStatusType(final int id) {
		return OrderStatusType(UserActionStatusTypeEnum.fromInt(id));
	}

	public static EnumOptionData OrderStatusType(final UserActionStatusTypeEnum type) {
		final String codePrefix = "deposit.interest.compounding.period.";
		EnumOptionData optionData = null;
		switch (type) {
		case ACTIVATION:
			optionData = new EnumOptionData(UserActionStatusTypeEnum.ACTIVATION.getValue().longValue(), codePrefix + UserActionStatusTypeEnum.ACTIVATION  .getCode(), "ACTIVATION");
			break;
		case RECONNECTION:
			optionData = new EnumOptionData(UserActionStatusTypeEnum.RECONNECTION.getValue().longValue(), codePrefix + UserActionStatusTypeEnum.RECONNECTION.getCode(), "RECONNECTION");
			break;

		case DISCONNECTION:
			optionData = new EnumOptionData(UserActionStatusTypeEnum.DISCONNECTION.getValue().longValue(), codePrefix + UserActionStatusTypeEnum.DISCONNECTION.getCode(), "DISCONNECTION");
			break;
			
		
		default:
			optionData = new EnumOptionData(StatusTypeEnum.INVALID.getValue().longValue(), StatusTypeEnum.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
