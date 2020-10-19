package com.yunseong.project.sagaparticipants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterWeClassCommand extends ProjectCommand {

    private long weClassId;

    public RegisterWeClassCommand(long projectId, long weClassId) {
        super(projectId);
        this.weClassId = weClassId;
    }
}
