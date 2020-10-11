package com.yunseong.weclass.domain;

import com.yunseong.weclass.api.events.WeClassCreatedEvent;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class WeClass {

    @Id
    @GeneratedValue
    private Long id;

    private String notice;

    @OneToMany(mappedBy = "weClass")
    private List<Report> reports = new ArrayList<>();

    public WeClass() {
        this.notice = "WeClass에 오신걸 환영합니다.";
    }

    public static ResultWithEvents<WeClass> create() {
        return new ResultWithEvents<>(new WeClass(), new WeClassCreatedEvent());
    }
}
