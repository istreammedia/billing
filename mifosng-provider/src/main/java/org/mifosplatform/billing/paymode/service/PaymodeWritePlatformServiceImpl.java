package org.mifosplatform.billing.paymode.service;

import java.util.Map;

import org.mifosplatform.billing.paymode.data.McodeData;
import org.mifosplatform.billing.paymode.domain.Paymode;
import org.mifosplatform.billing.paymode.domain.PaymodeRepository;
import org.mifosplatform.billing.paymode.exception.PaymodeNotFoundException;
import org.mifosplatform.billing.paymode.serialization.PaymodeCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymodeWritePlatformServiceImpl implements
		PaymodeWritePlatformService {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory
			.getLogger(PaymodeWritePlatformServiceImpl.class);

	private final PlatformSecurityContext context;
	private final PaymodeRepository paymodeRepository;
	private final PaymodeCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final PaymodeReadPlatformService readPlatformService;

	@Autowired
	public PaymodeWritePlatformServiceImpl(
			final PlatformSecurityContext context,
			final PaymodeRepository paymodeRepository,
			PaymodeReadPlatformService readPlatformService,
			final PaymodeCommandFromApiJsonDeserializer fromApiJsonDeserializer) {
		this.context = context;
		this.paymodeRepository = paymodeRepository;
		this.fromApiJsonDeserializer = fromApiJsonDeserializer;
		this.readPlatformService=readPlatformService;
	}

	@Transactional
	@Override
	public CommandProcessingResult createPaymode(JsonCommand command) {

		try {
			context.authenticatedUser();
			this.fromApiJsonDeserializer.validateForCreate(command.json());
			final McodeData Paymodedata = this.readPlatformService.retrievePaymodeCode(command);
			final Paymode paymode = Paymode.fromJson(command,Paymodedata.getId());
			this.paymodeRepository.save(paymode);

			return new CommandProcessingResultBuilder()
					.withCommandId(command.commandId())
					.withEntityId(paymode.getId()).build();
		} catch (DataIntegrityViolationException dve) {
			return CommandProcessingResult.empty();
		}
	}

	@Override
	public CommandProcessingResult updatePaymode(Long paymodeId,
			JsonCommand command) {
		try {
			context.authenticatedUser();
			this.fromApiJsonDeserializer.validateForUpdate(command.json());
			final McodeData Paymodedata = this.readPlatformService.retrievePaymodeCode(command);
			final Paymode paymode = retrievePaymodeBy(paymodeId);
			final Map<String, Object> changes = paymode.update(command,Paymodedata.getId());

			if (!changes.isEmpty()) {
				this.paymodeRepository.save(paymode);
			}

			return new CommandProcessingResultBuilder() //
					.withCommandId(command.commandId()) //
					.withEntityId(paymodeId) //
					.with(changes) //
					.build();
		} catch (DataIntegrityViolationException dve) {
			handleCodeDataIntegrityIssues(command, dve);
			return null;
		}
	}

	private void handleCodeDataIntegrityIssues(JsonCommand command,
			DataIntegrityViolationException dve) {
		Throwable realCause = dve.getMostSpecificCause();
		if (realCause.getMessage().contains("code_name_org")) {
			final String name = command.stringValueOfParameterNamed("codeValue");
			throw new PlatformDataIntegrityException(
					"error.msg.code.duplicate.name",
					"A code with paymode_code '" + name + "' already exists");
		}

		logger.error(dve.getMessage(), dve);
		throw new PlatformDataIntegrityException(
				"error.msg.cund.unknown.data.integrity.issue",
				"Unknown data integrity issue with resource: "
						+ realCause.getMessage());
	}

	@SuppressWarnings("unused")
	private Paymode retrievePaymodeBy(Long PaymodeId) {
		final Paymode paymode = this.paymodeRepository.findOne(PaymodeId);
		if (paymode == null) {
			throw new PaymodeNotFoundException(PaymodeId.toString());
		}
		return paymode;
	}

	@Override
	public CommandProcessingResult deletePaymode(Long PaymodeId) {
		context.authenticatedUser();

		Paymode paymode = this.paymodeRepository.findOne(PaymodeId);

		if (paymode == null) {
			throw new PaymodeNotFoundException(PaymodeId.toString());
		}

		this.paymodeRepository.delete(paymode);
		//paymode.delete();

		return new CommandProcessingResultBuilder().withEntityId(PaymodeId)
				.build();
	}

}
