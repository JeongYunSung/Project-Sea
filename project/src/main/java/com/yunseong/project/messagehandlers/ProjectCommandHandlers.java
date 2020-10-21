package com.yunseong.project.messagehandlers;

import com.yunseong.project.api.ProjectServiceChannels;
import com.yunseong.project.sagaparticipants.*;
import com.yunseong.project.service.ProjectService;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

public class ProjectCommandHandlers {

    @Autowired
    private ProjectService projectService;

    public CommandHandlers commandHandler() {
        return SagaCommandHandlersBuilder
                .fromChannel(ProjectServiceChannels.projectServiceChannel)
                .onMessage(RejectProjectCommand.class, this::rejectProject)
                .onMessage(RegisterWeClassCommand.class, this::registerWeClass)
                .onMessage(StartProjectCommand.class, this::startProject)

                .onMessage(ProjectBeginCancelCommand.class, this::cancelProject)
                .onMessage(ProjectUndoBeginCancelCommand.class, this::undoCancelProject)
                .onMessage(ProjectConfirmCancelCommand.class, this::confirmCancelProject)
                .build();
    }

    public Message cancelProject(CommandMessage<ProjectBeginCancelCommand> cm) {
        if(this.projectService.cancelProject(cm.getCommand().getProjectId()))
            return withSuccess();
        else
            return withFailure();
    }

    public Message undoCancelProject(CommandMessage<ProjectUndoBeginCancelCommand> cm) {
        this.projectService.undoCancelProject(cm.getCommand().getProjectId());
        return withSuccess();
    }

    public Message confirmCancelProject(CommandMessage<ProjectConfirmCancelCommand> cm) {
        this.projectService.cancelledProject(cm.getCommand().getProjectId());
        return withSuccess();
    }

    public Message startProject(CommandMessage<StartProjectCommand> cm) {
        long projectId = cm.getCommand().getProjectId();
        this.projectService.approveProject(projectId);
        return withSuccess();
    }

    public Message registerWeClass(CommandMessage<RegisterWeClassCommand> cm) {
        long projectId = cm.getCommand().getProjectId();
        long weClassId = cm.getCommand().getWeClassId();
        this.projectService.registerWeClass(projectId, weClassId);
        return withSuccess();
    }

    public Message rejectProject(CommandMessage<RejectProjectCommand> cm) {
        long projectId = cm.getCommand().getProjectId();
        this.projectService.rejectProject(projectId);
        return withSuccess();
    }
}
