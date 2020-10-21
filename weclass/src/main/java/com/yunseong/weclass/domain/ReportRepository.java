package com.yunseong.weclass.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r from Report r where r.weClass.id = :id and r.delete = false")
    Page<Report> findAllReportByWeClassId(long id, Pageable pageable);
}
