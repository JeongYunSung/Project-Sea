package com.yunseong.project.sagaparticipants;

import com.yunseong.project.api.TeamServiceChannels;
import com.yunseong.project.api.command.ApproveTeamCommand;
import com.yunseong.project.api.command.CreateTeamCommand;
import com.yunseong.project.api.command.CreateTeamReply;
import com.yunseong.project.api.command.RejectTeamCommand;
import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class TeamProxyService {

    public final CommandEndpoint<ApproveTeamCommand> approve = CommandEndpointBuilder
            .forCommand(ApproveTeamCommand.class)
            .withChannel(TeamServiceChannels.teamServiceChannel)
            .withReply(Success.class)
            .build();

    public final CommandEndpoint<RejectTeamCommand> reject = CommandEndpointBuilder
            .forCommand(RejectTeamCommand.class)
            .withChannel(TeamServiceChannels.teamServiceChannel)
            .withReply(Success.class)
            .build();

    public final CommandEndpoint<CreateTeamCommand> create = CommandEndpointBuilder
            .forCommand(CreateTeamCommand.class)
            .withChannel(TeamServiceChannels.teamServiceChannel)
            .withReply(CreateTeamReply.class)
            .build();
}
