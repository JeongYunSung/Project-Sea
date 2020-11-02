package com.yunseong.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username = :username")
    Optional<Member> findByUsername(String username);

    @Query("select m from Member m where m.username = :nickname")
    Optional<Member> findByNickname(String nickname);

    @Query("select m from Member m where m.username = :username or m.nickname = :nickname")
    Optional<Member> findByNicknameOrUsername(String username, String nickname);
}
