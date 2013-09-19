package org.mifosplatform.billing.paymode.data;

import org.mifosplatform.billing.paymode.domain.CategoryType;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

	public class SavingCategoryEnumaration {

		public static EnumOptionData interestCompoundingPeriodType(final int id) {
			return interestCompoundingPeriodType(CategoryType.fromInt(id));
		}

		public static EnumOptionData interestCompoundingPeriodType(final CategoryType type) {
			final String codePrefix = "deposit.interest.compounding.period.";
			EnumOptionData optionData = null;
			switch (type) {
			case DIRECT_DEBIT:
				optionData = new EnumOptionData(CategoryType.DIRECT_DEBIT.getValue().longValue(), codePrefix + CategoryType.DIRECT_DEBIT.getCode(), "DIRECT");
				break;
			case CASH:
				optionData = new EnumOptionData(CategoryType.CASH.getValue().longValue(), codePrefix + CategoryType.CASH.getCode(), "cash");
				break;
			case CREDIT_CARD:
				optionData = new EnumOptionData(CategoryType.CREDIT_CARD.getValue().longValue(), codePrefix + CategoryType.CREDIT_CARD.getCode(), "credit card");
				break;
			case CHEQUE:
				optionData = new EnumOptionData(CategoryType.CHEQUE.getValue().longValue(), codePrefix + CategoryType.CHEQUE.getCode(), "cheque");
				break;
			default:
				optionData = new EnumOptionData(CategoryType.INVALID.getValue().longValue(), CategoryType.INVALID.getCode(), "Invalid");
				break;
			}
			return optionData;
		}
	}