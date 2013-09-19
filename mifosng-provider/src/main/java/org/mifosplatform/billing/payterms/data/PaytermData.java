package org.mifosplatform.billing.payterms.data;

public class PaytermData {

	private Long id;
	private String paytermtype;
	private String duration;
	private String planType;
public PaytermData(final Long id,final String paytermtype, String duration, String planType)
{
this.id=id;
this.paytermtype=paytermtype;
this.duration=duration;
this.planType=planType;
}
public Long getId() {
	return id;
}
public String getPaytermtype() {
	return paytermtype;
}
/**
 * @return the duration
 */
public String getDuration() {
	return duration;
}
/**
 * @return the planType
 */
public String getPlanType() {
	return planType;
}


}
