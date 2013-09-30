package org.mifosplatform.infrastructure.jobs.handler;

import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.jobs.service.SchedularWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteJobDetailCommandhandler implements NewCommandSourceHandler {

	private final SchedularWritePlatformService schedularWritePlatformService;

    @Autowired
    public DeleteJobDetailCommandhandler(final SchedularWritePlatformService writePlatformService) {
        this.schedularWritePlatformService = writePlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.schedularWritePlatformService.deleteJob(command.entityId());
    }
}