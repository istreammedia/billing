package org.mifosplatform.billing.payments.service;
import java.math.BigDecimal;
import java.util.List;

import org.mifosplatform.billing.address.data.AddressData;
import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;
import org.mifosplatform.billing.clientbalance.domain.ClientBalance;
import org.mifosplatform.billing.clientbalance.domain.ClientBalanceRepository;
import org.mifosplatform.billing.clientbalance.service.ClientBalanceReadPlatformService;
import org.mifosplatform.billing.clientbalance.service.UpdateClientBalance;
import org.mifosplatform.billing.payments.domain.ChequePayment;
import org.mifosplatform.billing.payments.domain.ChequePaymentRepository;
import org.mifosplatform.billing.payments.domain.Payment;
import org.mifosplatform.billing.payments.domain.PaymentRepository;
import org.mifosplatform.billing.payments.paypal.service.PaypalReadPlatformService;
import org.mifosplatform.billing.payments.serialization.PaymentCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.transactionhistory.service.TransactionHistoryWritePlatformService;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentWritePlatformServiceImpl implements
		PaymentWritePlatformService {

	private final static Logger logger = LoggerFactory
			.getLogger(PaymentWritePlatformServiceImpl.class);

	private final PlatformSecurityContext context;
	private final PaymentRepository paymentRepository;
	private final PaymentCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final ClientBalanceReadPlatformService clientBalanceReadPlatformService;
	private final ClientBalanceRepository clientBalanceRepository;
	private final UpdateClientBalance updateClientBalance;
	private final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService;
	private final ChequePaymentRepository chequePaymentRepository;
	private final PaypalReadPlatformService paypalReadPlatformService;

	@Autowired
	public PaymentWritePlatformServiceImpl(final PlatformSecurityContext context,final PaymentRepository paymentRepository,
			final PaymentCommandFromApiJsonDeserializer fromApiJsonDeserializer,final ClientBalanceReadPlatformService clientBalanceReadPlatformService,
			final ClientBalanceRepository clientBalanceRepository,final ChequePaymentRepository chequePaymentRepository,
			final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService,final PaypalReadPlatformService paypalReadPlatformService,
			final UpdateClientBalance updateClientBalance) {
		this.context = context;
		this.paymentRepository = paymentRepository;
		this.fromApiJsonDeserializer = fromApiJsonDeserializer;
		this.clientBalanceReadPlatformService=clientBalanceReadPlatformService;
		this.clientBalanceRepository=clientBalanceRepository;
		this.transactionHistoryWritePlatformService = transactionHistoryWritePlatformService;
		 this.updateClientBalance= updateClientBalance;
		 this.chequePaymentRepository=chequePaymentRepository;
		 this.paypalReadPlatformService=paypalReadPlatformService;
	}

	@Override
	public CommandProcessingResult createPayment(JsonCommand command) {
		try {
			context.authenticatedUser();

			this.fromApiJsonDeserializer.validateForCreate(command.json());
			List<ClientBalanceData> clientBalancedatas = clientBalanceReadPlatformService.retrieveAllClientBalances(command.entityId());
			
			Long id=Long.valueOf(-1);
			if(clientBalancedatas.size() == 1)
				id= createPayments(clientBalancedatas.get(0).getId(),command.entityId(),command);											
			else
			id=	createPayments(command.entityId(),command.entityId(),command);
			
			if(command.stringValueOfParameterNamed("isChequeSelected").equalsIgnoreCase("yes")){
				ChequePayment chequePayment = ChequePayment.fromJson(command);
				chequePayment.setPaymentId(id);
				chequePaymentRepository.save(chequePayment);
			}
			
			
			return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(id).build();
		} catch (DataIntegrityViolationException dve) {
			return CommandProcessingResult.empty();
		}
	}
	

	@SuppressWarnings("unused")
	@Transactional
	@Override
	public Long createPayments(Long clientBalanceid, Long clientid,JsonCommand command) {
		// TODO Auto-generated method stub
                                                                              
		try {
			this.context.authenticatedUser();
			// AdjustmentCommandValidator validator=new AdjustmentCommandValidator(command);
			// validator.validateForCreate();

			Payment payment = null;
			payment  = Payment.fromJson(command,clientid);
			this.paymentRepository.saveAndFlush(payment);


			/* Manoj code for updating client balance */
              
			ClientBalance clientBalance = null;
			if(clientBalanceid!=null)
			clientBalance = clientBalanceRepository.findOne(clientBalanceid);

			if(clientBalance != null){

				// clientBalance = updateClientBalance.doUpdateClientBalance(command.getAdjustment_type(),command.getAmount_paid(),clientid,clientBalance);
				clientBalance = updateClientBalance.doUpdatePaymentClientBalance(clientid,command,clientBalance);

			}else if(clientBalance == null){

				clientBalance = updateClientBalance.createPaymentClientBalance(clientid,command, clientBalance);
			}

			updateClientBalance.saveClientBalanceEntity(clientBalance);
			
			transactionHistoryWritePlatformService.saveTransactionHistory(payment.getClientId(), "Payment", payment.getPaymentDate(),"AmountPaid:"+payment.getAmountPaid(),"PayMode:"+payment.getPaymodeCode(),"Remarks:"+payment.getRemarks(),"PaymentID:"+payment.getId());
			return payment.getId();

		} catch (DataIntegrityViolationException dve) {
			return Long.valueOf(-1);
		}
	}

	@Override
	public CommandProcessingResult createPaypalPayment(JsonCommand command) {

		
		try {
			context.authenticatedUser();
			String emailId=null;
            Long id = null;
          //  this.fromApiJsonDeserializer.validateForCreate(command.json());
            final BigDecimal amountPaid = command.bigDecimalValueOfParameterNamed("amountPaid");
            Long clientId=this.paypalReadPlatformService.retrieveClientId(command);
            List<ClientBalanceData> clientBalancedatas = clientBalanceReadPlatformService.retrieveAllClientBalances(clientId);
			id= createPayments(clientBalancedatas.get(0).getId(),clientId,command);					
			return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(id).build();
		} catch (DataIntegrityViolationException dve) {
			return CommandProcessingResult.empty();
		}
		
	
	}


}


