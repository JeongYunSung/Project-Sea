package com.yunseong.project.api.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateProjectRequest {

    private String username;
    private String subject;
    private String content;
    private Integer minPeople;
    private Integer maxPeople;
}
