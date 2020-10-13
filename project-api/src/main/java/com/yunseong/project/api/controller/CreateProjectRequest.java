package com.yunseong.project.api.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateProjectRequest {

    private long weClassId;
    private long teamId;
    private String subject;
    private String content;
}
