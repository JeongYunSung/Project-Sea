package com.yunseong.weclass.messagehandlers;

import com.yunseong.weclass.api.WeClassServiceChannels;
import com.yunseong.weclass.api.command.CreateWeClassCommand;
import com.yunseong.weclass.api.command.CreateWeClassReply;
import com.yunseong.weclass.domain.WeClass;
import com.yunseong.weclass.service.WeClassService;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.sagas.participant.SagaReplyMessageBuilder.withLock;

public class WeClassCommandHandler {

    @Autowired
    private WeClassService weClassService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(WeClassServiceChannels.weClassServiceChannel)
                .onMessage(CreateWeClassCommand.class, this::createWeClass)
                .build();
    }

    private Message createWeClass(CommandMessage<CreateWeClassCommand> commandMessage) {
        try {
            WeClass weClass = this.weClassService.createWeClass(commandMessage.getCommand().getProjectId());
            CreateWeClassReply reply = new CreateWeClassReply(weClass.getId());
            return withLock(WeClass.class, 5).withSuccess(reply);
        } catch (Exception e) {
            return withFailure();
        }
    }
}
