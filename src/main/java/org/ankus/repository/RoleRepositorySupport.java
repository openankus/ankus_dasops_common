package org.ankus.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.ankus.model.Role;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.ankus.model.QUser.user;
import static org.ankus.model.QRole.role;

@Repository
public class RoleRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public RoleRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Role.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public PageImpl list(int start, int length, String column, String sort, String state, String text, String udate){
        QueryResults<Role> queryResult = jpaQueryFactory
                .selectFrom(role)
                .where(
                        role.updateDateTime.between(
                                LocalDate.parse(udate.split(" ")[0], DateTimeFormatter.ISO_DATE).atTime(0,0),
                                LocalDate.parse(udate.split(" ")[1], DateTimeFormatter.ISO_DATE).atTime(23,59)
                        ),
                        roleLoginId(state, text),
                        roleName(state, text)
                )
                .orderBy(
                        getOrderSpecifier(column,sort)
                                .stream().toArray(OrderSpecifier[]::new)
                )
                .offset(start)
                .limit(length)
                .fetchResults();
        List<Role> content = queryResult.getResults();
        return new PageImpl(content);
    }
    public long total(String state, String text, String udate) {
        return jpaQueryFactory
                .selectFrom(role)
                .where(
                        role.updateDateTime.between(
                                LocalDate.parse(udate.split(" ")[0], DateTimeFormatter.ISO_DATE).atTime(0,0),
                                LocalDate.parse(udate.split(" ")[1], DateTimeFormatter.ISO_DATE).atTime(23,59)
                        ),
                        roleLoginId(state, text),
                        roleName(state, text)
                )
                .fetchCount();
    }

    private BooleanExpression roleLoginId(String type, String keyword) {
        if(type != null) return type.equals("loginId") ? user.loginId.contains(keyword) : null;
        else return null;
    }

    private BooleanExpression roleName(String type, String keyword) {
        if(type != null) return type.equals("name") ? user.name.contains(keyword) : null;
        else return null;
    }

    private List<OrderSpecifier> getOrderSpecifier(String column,String sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        // Sort
        Order direction = sort.equals("asc") ? Order.ASC : Order.DESC;
        switch (column) {
            case "loginId":
                OrderSpecifier<?> orderName = getSortedColumn(direction, user, "logId");
                orders.add(orderName);
                break;
            case "name":
                OrderSpecifier<?> orderCreateUser = getSortedColumn(direction, user, "name");
                orders.add(orderCreateUser);
                break;
            case "updateDateTime":
                OrderSpecifier<?> orderUpdateDateTime = getSortedColumn(direction, user, "enabled");
                orders.add(orderUpdateDateTime);
                break;
            default:
                break;
        }

        return orders;
    }

    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

}
