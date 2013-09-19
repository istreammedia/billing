package org.mifosplatform.billing.message.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.billing.message.data.BillingMessageData;
import org.mifosplatform.billing.plan.domain.Plan;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_message_params")
public class BillingMessageParam extends AbstractPersistable<Long>{

	/*CREATE TABLE `b_message_params` (
	  `id` int(20) NOT NULL AUTO_INCREMENT,
	  `msgtemplate_id` bigint(20) NOT NULL,
	  `parameter_name` varchar(120) NOT NULL,
	  `sequence_no` bigint(20) NOT NULL,
	  PRIMARY KEY (`id`),
	  KEY `fk_bmt_id` (`msgtemplate_id`),
	  CONSTRAINT `fk_bmt_id` FOREIGN KEY (`msgtemplate_id`) REFERENCES `b_message_template` (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;*/
	/*@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;*/

	@ManyToOne
    @JoinColumn(name="msgtemplate_id")
    private BillingMessageTemplate billingMessageTemplate;
	
	@Column(name = "parameter_name")
	private String parameterName;

	@Column(name="sequence_no")
	private Long sequenceNo;
	
	public BillingMessageParam(){
		//default-constructor
	}

	public BillingMessageParam(Long sequenceNo, String parameterName) {
		
		this.sequenceNo=sequenceNo;
		this.parameterName=parameterName;
	}
	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public Long getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public void update(BillingMessageTemplate billingMessageTemplate) {

		this.billingMessageTemplate=billingMessageTemplate;
	}

	/*public Map<String, Object> updateMessageParam(JsonCommand command,
			ArrayList<String> data) {
		// TODO Auto-generated method stub
		final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(
				1);

		//this.codeId = id.intValue();
		
		final String codeValue = "code_value";
		if (command.isChangeInStringParameterNamed(codeValue,
				this.codeValue)) {
			final String newValue = command
					.stringValueOfParameterNamed("code_value");
			actualChanges.put(codeValue, newValue);
			this.codeValue = StringUtils.defaultIfEmpty(newValue, null);
		}
		*/
	/*	
		return null;
	}
	*/
}
