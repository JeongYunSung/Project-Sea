package com.yunseong.project.history.domain;


import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.List;

@Getter
@RedisHash("project")
public class ProjectHistory {

    @Id
    private Long id;

    private Long weClassId;

    private ProjectInfo projectInfo;

    private List<Member> members;
}
