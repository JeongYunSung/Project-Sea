package com.yunseong.weclass.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r from Report r inner join r.weClass w where w.id = :id and w.isDelete = false and r.isDelete = false")
    Page<Report> findAllReportByWeClassId(long id, Pageable pageable);

    @Query("select r from Report r inner join r.weClass w where r.id = :id and w.isDelete = false and r.isDelete = false")
    Optional<Report> findOpenedById(long id);
}
