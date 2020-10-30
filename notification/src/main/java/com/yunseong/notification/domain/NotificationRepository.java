package com.yunseong.notification.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select n from Notification n where n.username = :username order by n.id desc",
    countQuery = "select n from Notification n where n.username = :username")
    Page<Notification> findByUsername(String username, Pageable pageable);
}
