package org.ankus.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.ankus.model.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.ankus.model.QUser.user;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public UserRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(User.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public PageImpl list(int start, int length, String column, String sort, String state, String text,String enabled, String udate){
        QueryResults<User> queryResult = jpaQueryFactory
                .selectFrom(user)
                .where(
                        user.updateDateTime.between(
                                LocalDate.parse(udate.split(" ")[0], DateTimeFormatter.ISO_DATE).atTime(0,0),
                                LocalDate.parse(udate.split(" ")[1], DateTimeFormatter.ISO_DATE).atTime(23,59)
                        ),
                        userLoginId(state, text),
                        userName(state, text),
                        userEnabled(enabled)
                )
                .orderBy(
                        getOrderSpecifier(column,sort)
                                .stream().toArray(OrderSpecifier[]::new)
                )
                .offset(start)
                .limit(length)
                .fetchResults();
        List<User> content = queryResult.getResults();
        return new PageImpl(content);
    }
    public long total(String state, String text, String udate) {
        return jpaQueryFactory
                .selectFrom(user)
                .where(
                        user.updateDateTime.between(
                                LocalDate.parse(udate.split(" ")[0], DateTimeFormatter.ISO_DATE).atTime(0,0),
                                LocalDate.parse(udate.split(" ")[1], DateTimeFormatter.ISO_DATE).atTime(23,59)
                        ),
                        userLoginId(state, text),
                        userName(state, text)
                )
                .fetchCount();
    }

    private BooleanExpression userLoginId(String type, String keyword) {
        if(type != null) return type.equals("loginId") ? user.loginId.contains(keyword) : null;
        else return null;
    }

    private BooleanExpression userName(String type, String keyword) {
        if(type != null) return type.equals("name") ? user.name.contains(keyword) : null;
        else return null;
    }
    private BooleanExpression userEnabled(String enabled) {
        return !enabled.equals("*") ? user.enabled.eq(enabled.equals("true")) : null;
    }

    private List<OrderSpecifier> getOrderSpecifier(String column,String sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        // Sort
        Order direction = sort.equals("asc") ? Order.ASC : Order.DESC;

        switch (column) {
            case "loginId":
                OrderSpecifier<?> orderLoginId = getSortedColumn(direction, user, "loginId");
                orders.add(orderLoginId);
                break;
            case "name":
                OrderSpecifier<?> orderName = getSortedColumn(direction, user, "name");
                orders.add(orderName);
                break;
            case "roleList":
                OrderSpecifier<?> orderRole = getSortedColumn(direction, user.roleList, "name");
                orders.add(orderRole);
                break;
            case "enabled":
                OrderSpecifier<?> orderEnabled = getSortedColumn(direction, user, "enabled");
                orders.add(orderEnabled);
                break;
            case "updateDateTime":
                OrderSpecifier<?> orderUpdateDateTime = getSortedColumn(direction, user, "updateDateTime");
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
