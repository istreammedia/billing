package org.mifosplatform.infrastructure.jobs.handler;

import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.jobs.service.SchedularWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateJobDetailCommandhandler implements NewCommandSourceHandler {

    private final SchedularWritePlatformService schedularWritePlatformService;

    @Autowired
    public CreateJobDetailCommandhandler(final SchedularWritePlatformService schedularWritePlatformService) {
        this.schedularWritePlatformService = schedularWritePlatformService;
    }

    @Override
    public CommandProcessingResult processCommand(JsonCommand command) {
        return this.schedularWritePlatformService.createNewJob(command);
    }

}
