package org.mifosplatform.billing.supplier.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;


@Entity
@Table(name="b_supplier")
public class Supplier extends AbstractAuditableCustom<AppUser, Long>{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="supplier_code", nullable=false, length=10)
	private String supplierCode;
	
	@Column(name="supplier_description", nullable=true, length=100)
	private String supplierDescription;
	
	@Column(name="supplier_address", nullable=true, length=100)
	private String supplierAddress;
	
	
	public Supplier() {}
	
	public Supplier(String supplierCode,String supplierDescription,String supplierAddress) {
		
		this.supplierCode = supplierCode;
		this.supplierDescription = supplierDescription;
		this.supplierAddress = supplierAddress;
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

	public static Supplier formJson(JsonCommand command) {
		final String supplierCode = command.stringValueOfParameterNamed("supplierCode");
		final String supplierDescription = command.stringValueOfParameterNamed("supplierDescription");
		final String supplierAddress =command.stringValueOfParameterNamed("supplierAddress");
		return new Supplier(supplierCode, supplierDescription, supplierAddress);
	}
}
