package org.mifosplatform.billing.plan.data;

public class BillRuleData {

	final String billruleOptions;
	final Long id;
	private String value;
	public BillRuleData(final Long id,final String options, String value) {
		this.id=id;
		this.billruleOptions=options;
		this.value=value;
	}
	public String getBillruleOptions() {
		return billruleOptions;
	}
	public Long getId() {
		return id;
	}
	public String getValue() {
		return value;
	}


}
