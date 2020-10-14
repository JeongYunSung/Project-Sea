package com.yunseong.weclass.service;

import com.yunseong.weclass.domain.WeClass;
import com.yunseong.weclass.domain.WeClassRepository;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeClassService {

    @Autowired
    private WeClassRepository weClassRepository;

    public WeClass createWeClass(long projectId) {
        ResultWithEvents<WeClass> rwe = WeClass.create(projectId);
        return this.weClassRepository.save(rwe.result);
    }
}
