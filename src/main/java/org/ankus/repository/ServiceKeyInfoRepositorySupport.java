package org.ankus.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.ankus.model.ServiceKeyInfo;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceKeyInfoRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     * @param jpaQueryFactory
     */
    public ServiceKeyInfoRepositorySupport(JPAQueryFactory jpaQueryFactory){
        super(ServiceKeyInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

}
