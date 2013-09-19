package org.mifosplatform.billing.paymode.handler;

import org.mifosplatform.billing.paymode.service.PaymodeWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UpdatePaymodeCommandHandler implements NewCommandSourceHandler {

	private final PaymodeWritePlatformService writePlatformService;

	@Autowired
	public UpdatePaymodeCommandHandler(
			final PaymodeWritePlatformService writePlatformService) {
		this.writePlatformService = writePlatformService;
	}

	@Transactional
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return this.writePlatformService.updatePaymode(command.entityId(),command);
	}

}
