package com.project.doongdoong.domain.counsel.adapter.out.repository.querydsl;


import com.project.doongdoong.domain.counsel.adapter.out.entitiy.CounselEntity;
import com.project.doongdoong.domain.user.adapter.out.persistence.entity.UserEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy.QAnalysisEntity.analysisEntity;
import static com.project.doongdoong.domain.counsel.domain.QCounselEntity.counselEntity;

public class CounselCustomJpaRepositoryImpl implements CounselCustomJpaRepository {

    private final JPAQueryFactory queryFactory;

    public CounselCustomJpaRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CounselEntity> searchPageCounselList(UserEntity userEntity, Pageable pageable) {
        List<CounselEntity> content = queryFactory
                .selectFrom(counselEntity)
                .leftJoin(counselEntity.analysis, analysisEntity).fetchJoin()
                //.where(userEq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(counselEntity.count())
                .from(counselEntity);
        //.where(userEq(user));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression userEq(UserEntity userEntity) {
        return counselEntity.user.id.eq(userEntity.getId());
    }
}
