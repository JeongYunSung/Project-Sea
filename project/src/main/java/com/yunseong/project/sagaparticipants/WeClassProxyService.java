package com.yunseong.project.sagaparticipants;

import com.yunseong.weclass.api.WeClassServiceChannels;
import com.yunseong.weclass.api.command.CreateWeClassCommand;
import com.yunseong.weclass.api.command.CreateWeClassReply;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class WeClassProxyService {

    public final CommandEndpoint<CreateWeClassCommand> create = CommandEndpointBuilder
            .forCommand(CreateWeClassCommand.class)
            .withChannel(WeClassServiceChannels.weClassServiceChannel)
            .withReply(CreateWeClassReply.class)
            .build();
}
