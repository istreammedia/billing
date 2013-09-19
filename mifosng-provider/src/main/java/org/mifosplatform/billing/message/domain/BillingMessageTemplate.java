package org.mifosplatform.billing.message.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import org.mifosplatform.billing.inventory.domain.InventoryItemDetails;
import org.mifosplatform.billing.message.data.BillingMessageData;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.useradministration.domain.AppUser;

@Entity
@Table(name = "b_message_template")
public class BillingMessageTemplate extends AbstractAuditableCustom<AppUser, Long>{
	/*CREATE TABLE `b_message_template` (
			  `id` bigint(20) NOT NULL AUTO_INCREMENT,
			  `template_description` varchar(20) NOT NULL,
			  `subject` varchar(120) NOT NULL,
			  `header` varchar(255) NOT NULL,
			  `body` TEXT NOT NULL,  
			  `footer` varchar(255) DEFAULT NULL,  
			  `createdby_id` bigint(20) DEFAULT NULL,
			  `created_date` datetime DEFAULT NULL,
			  PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;*/
	/*@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;*/

	@Column(name = "template_description")
	private String templateDescription;

	@Column(name = "subject")
	private String subject;
	
	@Column(name = "header")
	private String header;

	@Column(name = "body")
	private String body;
	
	@Column(name="is_deleted")
	private char isDeleted='N';
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "billingMessageTemplate", orphanRemoval = true)
	private List<BillingMessageParam> details = new ArrayList<BillingMessageParam>();
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "billingMessageTemplate", orphanRemoval = true)
	private List<BillingMessage> messageData= new ArrayList<BillingMessage>();

	
	public BillingMessageTemplate(){
		//default-constructor
	}
	
	public BillingMessageTemplate(String templateDescription,String subject,String header,String body,String footer)
	{
		this.templateDescription=templateDescription;
		this.subject=subject;
		this.header=header;
		this.body=body;
		this.footer=footer;
		this.details=details;
	}

	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}


	public List<BillingMessageParam> getDetails() {
		return details;
	}

	public void setDetails(List<BillingMessageParam> details) {
		this.details = details;
	}

	@Column(name = "footer")
	private String footer;


     public static BillingMessageTemplate fromJson(JsonCommand command){
		
		//final JsonElement element = fromJsonHelper.parse(command.toString());
		String  templateDescription=command.stringValueOfParameterNamed("templateDescription");
		String subject=command.stringValueOfParameterNamed("subject");
		String header=command.stringValueOfParameterNamed("header");
		String body=command.stringValueOfParameterNamed("body");
		String footer=command.stringValueOfParameterNamed("footer");
		
		return new BillingMessageTemplate(templateDescription,subject,header,body,footer);
	}


    public void setBillingMessageParam(BillingMessageParam billingMessageParam) {
	billingMessageParam.update(this);
	this.details.add(billingMessageParam);
	
}


	public void setMessageData(BillingMessage billingMessage) {
		//this.messageData = messageData;
		billingMessage.setupdate(this);
	}

	public Map<String, Object> updateMessageTemplate(JsonCommand command) {
		// TODO Auto-generated method stub
		final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);
		final String template_description = "template_description";
		if (command.isChangeInStringParameterNamed(template_description,
				this.templateDescription)) {
			final String newValue = command
					.stringValueOfParameterNamed("template_description");
			actualChanges.put(template_description, newValue);
			this.templateDescription = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String subject = "subject";
		if (command.isChangeInStringParameterNamed(subject,
				this.subject)) {
			final String newValue = command
					.stringValueOfParameterNamed("subject");
			actualChanges.put(subject, newValue);
			this.subject = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String header = "header";
		if (command.isChangeInStringParameterNamed(header,
				this.header)) {
			final String newValue = command
					.stringValueOfParameterNamed("header");
			actualChanges.put(header, newValue);
			this.header = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String body = "body";
		if (command.isChangeInStringParameterNamed(body,
				this.body)) {
			final String newValue = command
					.stringValueOfParameterNamed("body");
			actualChanges.put(body, newValue);
			this.body = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String footer = "footer";
		if (command.isChangeInStringParameterNamed(footer,
				this.footer)) {
			final String newValue = command
					.stringValueOfParameterNamed("footer");
			actualChanges.put(footer, newValue);
			this.footer = StringUtils.defaultIfEmpty(newValue, null);
		}
		return actualChanges;
	}

	public void isDelete() {
	if(this.isDeleted!= 'Y'){
		this.isDeleted='Y';
		this.templateDescription=this.templateDescription+"_DELETED"+this.getId();
	}
		
		
		
	}
    
	
	
}
