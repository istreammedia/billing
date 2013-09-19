package org.mifosplatform.billing.supplier.data;

public class SupplierData {
	
	private Long id;
	private String supplierCode;
	private String supplierDescription;
	private String supplierAddress;
	
	
	public SupplierData() {}
	
	public SupplierData(Long id,String supplierCode,String supplierDescription,String supplierAddress) {
		this.id = id;
		this.supplierCode = supplierCode;
		this.supplierDescription = supplierDescription;
		this.supplierAddress = supplierAddress;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the supplierCode
	 */
	public String getSupplierCode() {
		return supplierCode;
	}

	/**
	 * @param supplierCode the supplierCode to set
	 */
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	/**
	 * @return the supplierDescription
	 */
	public String getSupplierDescription() {
		return supplierDescription;
	}

	/**
	 * @param supplierDescription the supplierDescription to set
	 */
	public void setSupplierDescription(String supplierDescription) {
		this.supplierDescription = supplierDescription;
	}

	/**
	 * @return the supplierAddress
	 */
	public String getSupplierAddress() {
		return supplierAddress;
	}

	/**
	 * @param supplierAddress the supplierAddress to set
	 */
	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

}
