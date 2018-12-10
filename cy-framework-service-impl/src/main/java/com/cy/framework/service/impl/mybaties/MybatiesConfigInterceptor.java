package com.cy.framework.service.impl.mybaties;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * 映射处理
 */
public abstract class MybatiesConfigInterceptor {
    private static final String rowBounds = "delegate.rowBounds";
    private static final String configuration = "delegate.configuration";
    private static final String objectFactory = "delegate.objectFactory";
    private static final String typeHandlerRegistry = "delegate.typeHandlerRegistry";
    private static final String resultSetHandler = "delegate.resultSetHandler";
    private static final String parameterHandler = "delegate.parameterHandler";
    private static final String mappedStatement = "delegate.mappedStatement";
    private static final String boundSql = "delegate.boundSql";
    public String dialect = "";
    public MetaObject metaObject;

    public MetaObject getMetaObject() {
        return metaObject;
    }

    public void setMetaObject(MetaObject metaObject) {
        this.metaObject = metaObject;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public RowBounds getRowBounds(MetaObject metaObject) {
        return (RowBounds) metaObject.getValue(rowBounds);
    }

    public RowBounds getConfiguration(MetaObject metaObject) {
        return (RowBounds) metaObject.getValue(configuration);
    }

    public DefaultObjectFactory getObjectFactory(MetaObject metaObject) {
        return (DefaultObjectFactory) metaObject.getValue(objectFactory);
    }

    public TypeHandlerRegistry getTypeHandlerRegistry(MetaObject metaObject) {
        return (TypeHandlerRegistry) metaObject.getValue(typeHandlerRegistry);
    }

    public DefaultResultSetHandler getResultSetHandler(MetaObject metaObject) {
        return (DefaultResultSetHandler) metaObject.getValue(resultSetHandler);
    }

    public DefaultParameterHandler getParameterHandler(MetaObject metaObject) {
        return (DefaultParameterHandler) metaObject.getValue(parameterHandler);
    }

    /**
     * 获取 mapper映射
     *
     * @param metaObject
     * @return
     */
    public MappedStatement getMappedStatement(MetaObject metaObject) {
        return (MappedStatement) metaObject.getValue(mappedStatement);
    }

    /**
     * 获取sql
     *
     * @param metaObject
     * @return
     */
    public BoundSql getBoundSql(MetaObject metaObject) {
        return (BoundSql) metaObject.getValue(boundSql);
    }
}
