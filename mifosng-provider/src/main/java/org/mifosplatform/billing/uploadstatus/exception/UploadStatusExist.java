package org.mifosplatform.billing.uploadstatus.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

@SuppressWarnings("serial")
public class UploadStatusExist extends AbstractPlatformDomainRuleException{

	public UploadStatusExist(final String serialNumber){
		super("Item is already existing with item SerialNumber", "Item is already existing with item SerialNumber:"+serialNumber,serialNumber);
	}
}