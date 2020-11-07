package com.yunseong.project.scheduler;

import com.yunseong.project.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class ProjectScheduler {

    private final ProjectService projectService;

    @Scheduled(cron = "0 0 0 * * *")
    public void task() {
        this.projectService.startBatch(LocalDate.now());
    }
}
