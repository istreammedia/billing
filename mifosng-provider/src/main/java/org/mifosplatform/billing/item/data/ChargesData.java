package org.mifosplatform.billing.item.data;

public class ChargesData {



		private final Long id;

		private final String chargeCode;
		private final String chargeDescription;

		public ChargesData(final Long id,final String chargeCode,final String chargeDescription)
		{

			this.chargeDescription=chargeDescription;
			this.id=id;
			this.chargeCode=chargeCode;
		}

		public ChargesData(Long id, String chargeCode) {
			this.chargeDescription=null;
			this.id=id;
			this.chargeCode=chargeCode;
		}

		public Long getId() {
			return id;
		}

		public String getChargeCode() {
			return chargeCode;
		}

		public String getChargeDescription() {
			return chargeDescription;
		}



	}
