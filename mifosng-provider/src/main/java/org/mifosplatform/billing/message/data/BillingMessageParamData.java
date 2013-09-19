package org.mifosplatform.billing.message.data;

/**
 * @author avinash
 *
 */
public class BillingMessageParamData {
	
	
	private final Long id;
	private final Long msgTemplateId;
	private final String parameterName;
	private final Long sequenceNo;
	
	public BillingMessageParamData(final Long id,final Long msgTemplateId,final String parameterName,final Long sequenceNo)
	{
		this.id=id;
		this.msgTemplateId=msgTemplateId;
		this.parameterName=parameterName;
		this.sequenceNo=sequenceNo;
	}

	public Long getId() {
		return id;
	}

	public Long getMsgTemplateId() {
		return msgTemplateId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public Long getSequenceNo() {
		return sequenceNo;
	}
	
	

}
