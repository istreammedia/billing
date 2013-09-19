package org.mifosplatform.billing.charge.data;

public class ChargeCodeData {
private Long id;
private String chargeCode;
public ChargeCodeData(Long id,String chargeCode)
{
	this.id=id;
	this.chargeCode=chargeCode;

}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getChargeCode() {
	return chargeCode;
}
public void setChargeCode(String chargeCode) {
	this.chargeCode = chargeCode;
}

}
