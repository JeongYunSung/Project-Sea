package com.yunseong.team.messagehandlers;

import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.api.TeamServiceChannels;
import com.yunseong.project.api.command.*;
import com.yunseong.team.domain.Team;
import com.yunseong.team.service.TeamService;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;
import static io.eventuate.tram.sagas.participant.SagaReplyMessageBuilder.withLock;

public class TeamCommandHandlers {

    @Autowired
    private TeamService teamService;

    public CommandHandlers commandHandler() {
        return SagaCommandHandlersBuilder
                .fromChannel(TeamServiceChannels.teamServiceChannel)
                .onMessage(CreateTeamCommand.class, this::createTeam)

                .onMessage(ApproveTeamCommand.class, this::approveTeam)
                .onMessage(RejectTeamCommand.class, this::rejectTeam)

                .onMessage(CancelTeamCommand.class, this::cancelTeam)
                .build();
    }

    private Message createTeam(CommandMessage<CreateTeamCommand> commandMessage) {
        try {
            CreateTeamCommand command = commandMessage.getCommand();
            Team team = this.teamService.createTeam(command.getProjectId(), command.getUsername(), command.getMinSize(), command.getMaxSize());
            return withLock(Team.class, team.getId()).withSuccess(new CreateTeamReply(team.getId()));
        } catch (Exception e) {
            return withFailure();
        }
    }

    private Message cancelTeam(CommandMessage<CancelTeamCommand> commandMessage) {
        if(this.teamService.cancel(commandMessage.getCommand().getTeamId()))
            return withSuccess();
        else
            return withFailure();
    }

    private Message approveTeam(CommandMessage<ApproveTeamCommand> commandMessage) {
        if(this.teamService.approveTeam(commandMessage.getCommand().getTeamId())) {
            return withSuccess();
        }else {
            return withFailure();
        }
    }

    private Message rejectTeam(CommandMessage<RejectTeamCommand> commandMessage) {
        this.teamService.rejectTeam(commandMessage.getCommand().getTeamId());
        return withSuccess();
    }
}
