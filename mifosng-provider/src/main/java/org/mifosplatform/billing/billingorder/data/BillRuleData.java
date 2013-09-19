package org.mifosplatform.billing.billingorder.data;

public class BillRuleData {

	final String billruleOptions;
	final Long id;
	public BillRuleData(final Long id,final String options) {
		this.id=id;
		this.billruleOptions=options;
	}
	public String getBillruleOptions() {
		return billruleOptions;
	}
	public Long getId() {
		return id;
	}


}
