package org.mifosplatform.billing.message.data;

import java.util.List;

import org.mifosplatform.billing.plan.data.ServiceData;

public class BillingMessageTemplateData {
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
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;
			
			`msgtemplate_id` bigint(20) NOT NULL,
  `parameter_name` varchar(120) NOT NULL,
  `sequence_no` bigint(20) NOT NULL,
			
*/
private final Long  id;
private final String templateDescription;
private final  String subject;
private final String header;
private final String body;
private final String footer;
private  final List<BillingMessageParamData> billingMessageParamData;




public List<BillingMessageParamData> getBillingMessageParamData() {
	return billingMessageParamData;
}


public BillingMessageTemplateData(Long  id,String templateDescription,String subject,String header,String body,String footer,List<BillingMessageParamData> billingMessageParamData)
{
	this.id=id;
	this.templateDescription=templateDescription;
	this.subject=subject;
	this.header=header;
	this.body=body;
	this.footer=footer;
	this.billingMessageParamData=billingMessageParamData;

}
public Long getId() {
	return id;
}
public String getTemplateDescription() {
	return templateDescription;
}
public String getSubject() {
	return subject;
}
public String getHeader() {
	return header;
}
public String getBody() {
	return body;
}
public String getFooter() {
	return footer;
}
 
}
