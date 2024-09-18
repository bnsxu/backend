package com.example.meettify.entity.jwt;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTokenEntity is a Querydsl query type for TokenEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTokenEntity extends EntityPathBase<TokenEntity> {

    private static final long serialVersionUID = -1183854984L;

    public static final QTokenEntity tokenEntity = new QTokenEntity("tokenEntity");

    public final StringPath email = createString("email");

    public final StringPath grantType = createString("grantType");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final NumberPath<Long> tokenId = createNumber("tokenId", Long.class);

    public QTokenEntity(String variable) {
        super(TokenEntity.class, forVariable(variable));
    }

    public QTokenEntity(Path<? extends TokenEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTokenEntity(PathMetadata metadata) {
        super(TokenEntity.class, metadata);
    }

}

