package com.yunseong.project.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectCreatedEvent implements ProjectEvent {

    private ProjectDetail projectDetail;
}
