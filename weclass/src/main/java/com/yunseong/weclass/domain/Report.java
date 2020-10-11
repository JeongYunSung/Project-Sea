package com.yunseong.weclass.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Report {

    @Id
    @GeneratedValue
    private Long id;
    private String subject;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weclass_name")
    private WeClass weClass;

    public Report(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public void registerWeClass(WeClass weClass) {
        this.weClass = weClass;
        weClass.getReports().add(this);
    }
}
