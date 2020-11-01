package com.yunseong.board.domain;

import com.yunseong.member.api.controller.Permission;
import lombok.Getter;

@Getter
public enum BoardCategory {

    NOTICE(Permission.ANONYMOUS, Permission.ADMIN), DEPARTMENT(Permission.STUDENT, Permission.STUDENT), FREE(Permission.ANONYMOUS, Permission.USER);

    private final Permission readPermission;
    private final Permission writePermission;

    BoardCategory(Permission readPermission, Permission writePermission) {
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }
}
