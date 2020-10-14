package com.yunseong.project.messagehandlers;

import com.yunseong.project.api.ProjectServiceChannels;
import com.yunseong.project.sagaparticipants.RejectProjectCommand;
import com.yunseong.project.service.ProjectService;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

public class ProjectCommandHandlers {

    @Autowired
    private ProjectService projectService;

    public CommandHandlers commandHandler() {
        return SagaCommandHandlersBuilder
                .fromChannel(ProjectServiceChannels.projectServiceChannel)
                .onMessage(RejectProjectCommand.class, this::rejectProject)
                .build();
    }

    public Message rejectProject(CommandMessage<RejectProjectCommand> cm) {
        long projectId = cm.getCommand().getProjectId();
        this.projectService.rejectProject(projectId);
        return withSuccess();
    }
}
