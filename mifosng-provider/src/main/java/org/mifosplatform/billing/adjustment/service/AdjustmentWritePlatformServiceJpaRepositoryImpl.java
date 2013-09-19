package org.mifosplatform.billing.adjustment.service;

import java.util.List;

import org.mifosplatform.billing.adjustment.domain.Adjustment;
import org.mifosplatform.billing.adjustment.domain.AdjustmentRepository;
import org.mifosplatform.billing.adjustment.serializer.AdjustmentCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;
import org.mifosplatform.billing.clientbalance.domain.ClientBalance;
import org.mifosplatform.billing.clientbalance.domain.ClientBalanceRepository;
import org.mifosplatform.billing.clientbalance.service.ClientBalanceReadPlatformService;
import org.mifosplatform.billing.clientbalance.service.UpdateClientBalance;
import org.mifosplatform.billing.transactionhistory.service.TransactionHistoryWritePlatformService;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdjustmentWritePlatformServiceJpaRepositoryImpl implements
		AdjustmentWritePlatformService {

	private final org.mifosplatform.infrastructure.security.service.PlatformSecurityContext context;
	private final AdjustmentRepository adjustmentRepository;
	private final ClientBalanceRepository clientBalanceRepository;
	private final UpdateClientBalance updateClientBalance;
	private final ClientBalanceReadPlatformService clientBalanceReadPlatformService;
	private final AdjustmentReadPlatformService adjustmentReadPlatformService;
	private final AdjustmentCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService;

	@Autowired
	public AdjustmentWritePlatformServiceJpaRepositoryImpl(final PlatformSecurityContext context,final AdjustmentRepository adjustmentRepository,
			final ClientBalanceRepository clientBalanceRepository,final AdjustmentCommandFromApiJsonDeserializer fromApiJsonDeserializer,
			final UpdateClientBalance updateClientBalance,final ClientBalanceReadPlatformService clientBalanceReadPlatformService,
			final AdjustmentReadPlatformService adjustmentReadPlatformService,
			final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService) {
		this.context = context;
		this.adjustmentRepository = adjustmentRepository;
		this.clientBalanceRepository = clientBalanceRepository;
		this.updateClientBalance = updateClientBalance;
		this.clientBalanceReadPlatformService = clientBalanceReadPlatformService;
		this.adjustmentReadPlatformService = adjustmentReadPlatformService;
		this.fromApiJsonDeserializer = fromApiJsonDeserializer;
		this.transactionHistoryWritePlatformService = transactionHistoryWritePlatformService;
	}

	@SuppressWarnings("unused")
	@Transactional
	@Override
	public Long createAdjustment(Long id2, Long id, Long clientid,
			JsonCommand command) {
		// TODO Auto-generated method stub

		try {
			this.context.authenticatedUser();
			Adjustment adjustment = null;
			adjustment = Adjustment.fromJson(command);
			ClientBalance clientBalance = null;
			if (id != null)
				clientBalance = clientBalanceRepository.findOne(id);

			if (clientBalance != null) {
			clientBalance = updateClientBalance	.doUpdateAdjustmentClientBalance(command, clientBalance);

			} else if (clientBalance == null) {
				clientBalance = updateClientBalance.createAdjustmentClientBalance(command, clientBalance);
			}

			updateClientBalance.saveClientBalanceEntity(clientBalance);

			this.adjustmentRepository.saveAndFlush(adjustment);
			transactionHistoryWritePlatformService.saveTransactionHistory(adjustment.getClient_id(), "Adjustment", adjustment.getAdjustment_date(),"AmountPaid:"+adjustment.getAmount_paid(),"AdjustmentType:"+adjustment.getAdjustment_type(),"AdjustmentCode:"+adjustment.getAdjustment_code(),"Remarks:"+adjustment.getRemarks(),"AdjustmentID:"+adjustment.getId());
			return adjustment.getId();
		} catch (DataIntegrityViolationException dve) {
			return Long.valueOf(-1);
		}
	}

	@Override
	public CommandProcessingResult createAdjustments(JsonCommand command) {

		try {
			context.authenticatedUser();

			this.fromApiJsonDeserializer.validateForCreate(command.json());
			List<ClientBalanceData> clientBalancedatas = clientBalanceReadPlatformService
					.retrieveAllClientBalances(command.entityId());
			List<ClientBalanceData> adjustmentBalancesDatas = adjustmentReadPlatformService
					.retrieveAllAdjustments(command.entityId());
			Long id = Long.valueOf(-1);
			if (clientBalancedatas.size() == 1
					&& adjustmentBalancesDatas.size() == 1)
				id = createAdjustment(clientBalancedatas.get(0).getId(),
						adjustmentBalancesDatas.get(0).getId(),
						command.entityId(), command);
			else
				id = createAdjustment(command.entityId(), command.entityId(),
						command.entityId(), command);

			return new CommandProcessingResultBuilder()
					.withCommandId(command.commandId()).withEntityId(id)
					.build();
		} catch (DataIntegrityViolationException dve) {
			return CommandProcessingResult.empty();
		}
	}
}
