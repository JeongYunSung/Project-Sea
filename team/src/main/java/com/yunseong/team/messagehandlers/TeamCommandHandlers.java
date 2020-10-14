package com.yunseong.team.messagehandlers;

import com.yunseong.project.api.TeamServiceChannels;
import com.yunseong.project.api.command.ApproveTeamCommand;
import com.yunseong.project.api.command.RejectTeamCommand;
import com.yunseong.team.domain.TeamRejectException;
import com.yunseong.team.service.TeamService;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

public class TeamCommandHandlers {

    @Autowired
    private TeamService teamService;

    public CommandHandlers commandHandler() {
        return SagaCommandHandlersBuilder
                .fromChannel(TeamServiceChannels.teamServiceChannel)
                .onMessage(ApproveTeamCommand.class, this::approveTeam)
                .onMessage(RejectTeamCommand.class, this::rejectTeam)
                .build();
    }

    private Message approveTeam(CommandMessage<ApproveTeamCommand> commandMessage) {
        try {
            this.teamService.approveTeam(commandMessage.getCommand().getProjectId());
            return withSuccess();
        } catch (TeamRejectException e) {
            return withFailure();
        }
    }

    private Message rejectTeam(CommandMessage<RejectTeamCommand> commandMessage) {
        this.teamService.rejectTeam(commandMessage.getCommand().getProjectId());
        return withSuccess();
    }
}
