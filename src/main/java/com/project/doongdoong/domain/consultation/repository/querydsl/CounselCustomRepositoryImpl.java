package com.project.doongdoong.domain.consultation.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class CounselCustomRepositoryImpl implements CounselCustumRepository {

    private final JPAQueryFactory queryFactory;

    public CounselCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);


    }
}
