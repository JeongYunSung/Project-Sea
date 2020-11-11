package com.yunseong.project.scheduler;

import com.yunseong.project.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Component
@AllArgsConstructor
public class ProjectScheduler {

    private final ProjectService projectService;

    @Scheduled(cron = "0 0 0 * * *")
    public void task() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            this.projectService.startBatch(simpleDateFormat.parse(simpleDateFormat.format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
