package com.yunseong.weclass.controller;

import com.yunseong.weclass.domain.WeClass;
import com.yunseong.weclass.service.WeClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/weclass", consumes = MediaType.APPLICATION_JSON_VALUE)
public class WeClassController {

    @Autowired
    private WeClassService weClassService;

    @PostMapping("/{id}/report")
    public ResponseEntity<Long> createReport(@PathVariable long id, CreateReportRequest reportRequest) {
        WeClass report = this.weClassService.createReport(id, reportRequest.getWriter(), reportRequest.getSubject(), reportRequest.getContent());
        return new ResponseEntity<>(report.getId(), HttpStatus.CREATED);
    }
}
