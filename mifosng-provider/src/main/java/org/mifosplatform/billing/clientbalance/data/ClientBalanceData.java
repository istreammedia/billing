package org.mifosplatform.billing.clientbalance.data;

import java.math.BigDecimal;

public class ClientBalanceData {
private Long id;
private Long clientId;
private BigDecimal balanceAmount;

public ClientBalanceData(Long id,Long clientId,BigDecimal balanceAmount)
{
	this.id=id;
	this.clientId=clientId;
	this.balanceAmount=balanceAmount;
}

public ClientBalanceData(BigDecimal balance) {
	this.balanceAmount=balance;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Long getClientId() {
	return clientId;
}

public void setClientId(Long clientId) {
	this.clientId = clientId;
}

public BigDecimal getBalanceAmount() {
	return balanceAmount;
}

public void setBalanceAmount(BigDecimal balanceAmount) {
	this.balanceAmount = balanceAmount;
}


}
