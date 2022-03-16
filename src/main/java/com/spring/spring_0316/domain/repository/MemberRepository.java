package com.spring.spring_0316.domain.repository;

import com.spring.spring_0316.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByAccountId(String accountId);

    boolean existsByAccountId(String accountID);
}

