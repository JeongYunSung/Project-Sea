package com.yunseong.weclass.api.command;

import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateWeClassCommand implements Command {

    private long projectId;
}
