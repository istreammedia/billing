package org.mifosplatform.billing.paymode.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.payments.data.PaymentData;
import org.mifosplatform.billing.paymode.data.McodeData;
import org.mifosplatform.infrastructure.core.api.JsonCommand;

public interface PaymodeReadPlatformService {

	McodeData retrieveSinglePaymode(Long paymodeId);

	//Collection<PaymodeData> retrieveAllPaymodes();

	McodeData retrievePaymodeCode(JsonCommand command);

	Collection<McodeData> retrievemCodeDetails(String codeName);

	List<PaymentData> retrivePaymentsData(Long clientId);

}
