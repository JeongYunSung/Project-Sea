package com.yunseong.weclass.service;

import com.yunseong.weclass.domain.Report;
import com.yunseong.weclass.domain.WeClass;
import com.yunseong.weclass.domain.WeClassRepository;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class WeClassService {

    @Autowired
    private WeClassRepository weClassRepository;

    public WeClass createWeClass(long projectId) {
        ResultWithEvents<WeClass> rwe = WeClass.create(projectId);
        return this.weClassRepository.save(rwe.result);
    }

    public WeClass createReport(long weClassId, String username, String subject, String content) {
        WeClass weClass = this.weClassRepository.findById(weClassId).orElseThrow(() -> new EntityNotFoundException("해당 WeClass는 존재하지 않습니다."));
        Report report = new Report(username, subject, content);
        report.equals(weClass);
        return weClass;
    }
}
