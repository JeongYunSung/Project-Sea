package com.yunseong.project.sagas.cancelproject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CancelProjectSagaData {

    private long projectId;
    private long teamId;
    private String username;
}
