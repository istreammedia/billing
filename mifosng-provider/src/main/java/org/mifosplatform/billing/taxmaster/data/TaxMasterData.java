package org.mifosplatform.billing.taxmaster.data;

public class TaxMasterData {
private Long id;
private String taxCode;
public TaxMasterData(Long id,String taxCode)
{
	this.id=id;
	this.taxCode=taxCode;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getTaxCode() {
	return taxCode;
}
public void setTaxCode(String taxCode) {
	this.taxCode = taxCode;
}
}
