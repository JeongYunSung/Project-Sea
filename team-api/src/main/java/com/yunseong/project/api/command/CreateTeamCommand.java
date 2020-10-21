package com.yunseong.project.api.command;

import io.eventuate.tram.commands.common.Command;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateTeamCommand implements Command {

    private long projectId;
    private String username;
    private int minSize;
    private int maxSize;
}
