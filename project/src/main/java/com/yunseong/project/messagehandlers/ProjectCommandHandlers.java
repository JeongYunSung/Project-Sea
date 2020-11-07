package com.yunseong.project.messagehandlers;

import com.yunseong.project.api.ProjectServiceChannels;
import com.yunseong.project.domain.ProjectSimpleRevision;
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
                .onMessage(RegisterTeamCommand.class, this::registerTeam)
                .onMessage(RegisterBoardCommand.class, this::registerBoard)
                .onMessage(CreateProjectCommand.class, this::createProject)

                .onMessage(BeginReviseProjectCommand.class, this::reviseProject)
                .onMessage(UndoBeginReviseProjectCommand.class, this::undoReviseProject)
                .onMessage(ConfirmReviseProjectCommand.class, this::confirmReviseProject)

                .onMessage(CloseProjectCommand.class, this::closeProject)
                .onMessage(RejectProjectCommand.class, this::rejectProject)
                .onMessage(RegisterWeClassCommand.class, this::registerWeClass)
                .onMessage(StartProjectCommand.class, this::startProject)

                .onMessage(BeginCancelProjectCommand.class, this::cancelProject)
                .onMessage(UndoBeginCancelProjectCommand.class, this::undoCancelProject)
                .onMessage(ConfirmCancelProjectCommand.class, this::confirmCancelProject)

                .onMessage(BatchStartProjectCommand.class, this::startBatch)
                .onMessage(BatchUndoProjectCommand.class, this::undoBatch)
                .onMessage(BatchFinishedProjectCommand.class, this::finishBatch)
                .build();
    }

    public Message startBatch(CommandMessage<BatchStartProjectCommand> cm) {
        if(this.projectService.batchPending(cm.getCommand().getProjectIds())) {
            return withSuccess();
        }
        return withFailure();
    }

    public Message undoBatch(CommandMessage<BatchUndoProjectCommand> cm) {
        this.projectService.batchUndo(cm.getCommand().getProjectIds());
        return withSuccess();
    }

    public Message finishBatch(CommandMessage<BatchFinishedProjectCommand> cm) {
        if(this.projectService.batched(cm.getCommand().getProjectIds())) {
            return withSuccess();
        }
        return withFailure();
    }

    public Message reviseProject(CommandMessage<BeginReviseProjectCommand> cm) {
        long projectId = cm.getCommand().getProjectId();
        if(this.projectService.reviseProject(projectId))
            return withSuccess();
        else
            return withFailure();
    }

    public Message undoReviseProject(CommandMessage<UndoBeginReviseProjectCommand> cm) {
        this.projectService.undoCancelOrUndoReviseOrPostedProject(cm.getCommand().getProjectId());
        return withSuccess();
    }

    public Message confirmReviseProject(CommandMessage<ConfirmReviseProjectCommand> cm) {
        if(this.projectService.revisedProject(cm.getCommand().getProjectId(), new ProjectSimpleRevision(cm.getCommand().getSubject(), cm.getCommand().isPublic())))
            return withSuccess();
        return withFailure();
    }

    public Message registerTeam(CommandMessage<RegisterTeamCommand> cm) {
        long projectId = cm.getCommand().getProjectId();
        long teamId = cm.getCommand().getTeamId();
        this.projectService.registerTeam(projectId, teamId, cm.getCommand().getUsername());
        return withSuccess();
    }

    public Message createProject(CommandMessage<CreateProjectCommand> cm) {
        this.projectService.undoCancelOrUndoReviseOrPostedProject(cm.getCommand().getProjectId());
        return withSuccess();
    }

    public Message cancelProject(CommandMessage<BeginCancelProjectCommand> cm) {
        if(this.projectService.cancelProject(cm.getCommand().getProjectId()))
            return withSuccess();
        else
            return withFailure();
    }

    public Message undoCancelProject(CommandMessage<UndoBeginCancelProjectCommand> cm) {
        this.projectService.undoCancelOrUndoReviseOrPostedProject(cm.getCommand().getProjectId());
        return withSuccess();
    }

    public Message confirmCancelProject(CommandMessage<ConfirmCancelProjectCommand> cm) {
        if(this.projectService.cancelledProject(cm.getCommand().getProjectId()))
            return withSuccess();
        return withFailure();
    }

    public Message closeProject(CommandMessage<CloseProjectCommand> cm) {
        long projectId = cm.getCommand().getProjectId();
        if(this.projectService.closeProject(projectId))
            return withSuccess();
        else
            return withFailure();
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

    public Message registerBoard(CommandMessage<RegisterBoardCommand> cm) {
        long projectId = cm.getCommand().getProjectId();
        long boardId = cm.getCommand().getBoardId();
        this.projectService.registerBoard(projectId, boardId, cm.getCommand().getBoardDetail());
        return withSuccess();
    }

    public Message rejectProject(CommandMessage<RejectProjectCommand> cm) {
        long projectId = cm.getCommand().getProjectId();
        this.projectService.rejectProject(projectId);
        return withSuccess();
    }
}
