package com.yunseong.project.sagaparticipants;

import io.eventuate.tram.commands.common.Command;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ProjectCommand implements Command {

    private long projectId;
}
