package com.yunseong.weclass.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WeClassRepository extends JpaRepository<WeClass, Long> {

    @Query("select w from WeClass  w where w.id = :id and w.isDelete = false")
    Optional<WeClass> findOpenedById(long id);
}
