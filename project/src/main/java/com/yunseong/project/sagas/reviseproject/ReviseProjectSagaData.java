package com.yunseong.project.sagas.reviseproject;

import com.yunseong.project.domain.ProjectRevision;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ReviseProjectSagaData {

    private long projectId;
    private long teamId;
    private ProjectRevision projectRevision;
    private String username;
}
