package org.mifosplatform.billing.billingorder.data;

import java.math.BigDecimal;

public class InvoiceAmountIdentifier {
 
 private BigDecimal invoiceAmount;
 
 public InvoiceAmountIdentifier() {
  this.invoiceAmount = BigDecimal.ZERO;
 }
 
 public InvoiceAmountIdentifier(final BigDecimal invoiceAmount) {
  this.invoiceAmount = invoiceAmount;
 }

 public BigDecimal getInvoiceAmount() {
  return invoiceAmount;
 }

 public void setInvoiceAmount(BigDecimal invoiceAmount) {
  this.invoiceAmount = invoiceAmount;
 }

}