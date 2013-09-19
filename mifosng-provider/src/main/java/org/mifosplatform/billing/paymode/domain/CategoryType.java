package org.mifosplatform.billing.paymode.domain;

public enum CategoryType {

	DIRECT_DEBIT(0, "CategoryType.direct"), //
		CASH(1, "CategoryType.cash"), //
		CREDIT_CARD(2, "CategoryType.credit"),//
		CHEQUE(3,"CategoryType.cheque"),//
	   INVALID(3, "CategoryType.invalid");

	    private final Integer value;
		private final String code;

	    private CategoryType(final Integer value, final String code) {
	        this.value = value;
			this.code = code;
	    }

	    public Integer getValue() {
	        return this.value;
	    }

		public String getCode() {
			return code;
		}

		public static CategoryType fromInt(final Integer frequency) {

			CategoryType repaymentFrequencyType = CategoryType.INVALID;
			switch (frequency) {
			case 0:
				repaymentFrequencyType = CategoryType.DIRECT_DEBIT;
				break;
			case 1:
				repaymentFrequencyType = CategoryType.CASH;
				break;
			case 2:
				repaymentFrequencyType = CategoryType.CREDIT_CARD;
				break;
			case 3:
				repaymentFrequencyType = CategoryType.CHEQUE;
				break;

			default:
				repaymentFrequencyType = CategoryType.INVALID;
				break;
			}
			return repaymentFrequencyType;
		}
	}
