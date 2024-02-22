package org.ankus.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QServiceKeyInfo is a Querydsl query type for ServiceKeyInfo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QServiceKeyInfo extends EntityPathBase<ServiceKeyInfo> {

    private static final long serialVersionUID = 570260409L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QServiceKeyInfo serviceKeyInfo = new QServiceKeyInfo("serviceKeyInfo");

    public final DateTimePath<java.time.LocalDateTime> createDateTime = createDateTime("createDateTime", java.time.LocalDateTime.class);

    public final BooleanPath deleted = createBoolean("deleted");

    public final DateTimePath<java.time.LocalDateTime> deleteDateTime = createDateTime("deleteDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath serviceKey = createString("serviceKey");

    public final QUser user;

    public QServiceKeyInfo(String variable) {
        this(ServiceKeyInfo.class, forVariable(variable), INITS);
    }

    public QServiceKeyInfo(Path<? extends ServiceKeyInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QServiceKeyInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QServiceKeyInfo(PathMetadata metadata, PathInits inits) {
        this(ServiceKeyInfo.class, metadata, inits);
    }

    public QServiceKeyInfo(Class<? extends ServiceKeyInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

