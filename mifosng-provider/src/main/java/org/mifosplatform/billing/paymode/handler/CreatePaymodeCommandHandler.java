package org.mifosplatform.billing.paymode.handler;

import org.mifosplatform.billing.paymode.service.PaymodeWritePlatformService;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;


@Service
public class CreatePaymodeCommandHandler implements NewCommandSourceHandler {

	private final PaymodeWritePlatformService writePlatformService;

	@Autowired
	public CreatePaymodeCommandHandler(
			final PaymodeWritePlatformService writePlatformService) {
		this.writePlatformService = writePlatformService;
	}

	@Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return this.writePlatformService.createPaymode(command);
	}
}
