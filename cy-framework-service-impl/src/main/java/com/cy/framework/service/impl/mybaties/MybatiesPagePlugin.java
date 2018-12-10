package com.cy.framework.service.impl.mybaties;

import com.cy.framework.model.PageInfo;
import org.apache.ibatis.executor.statement.StatementHandler;

import java.util.Map;

/**
 * 分页处理
 */
public abstract class MybatiesPagePlugin extends MybatiesConfigInterceptor{
    /**
     * 获取分页sql - 如果要支持其他数据库，修改这里就可以
     *
     * @param sql
     * @param page
     * @return
     */
    private String getPageSql(String sql, PageInfo page) {
        StringBuilder pageSql = new StringBuilder(200);
        if ("postgresql".equals(dialect)) {
            pageSql.append(sql);
            pageSql.append(" limit " + page.getPageSize() + " offset "
                    + page.getStartRow());
        } else if ("mysql".equals(dialect)) {
            pageSql.append(sql);
            pageSql.append(" limit " + page.getStartRow() + ","
                    + page.getPageSize());
        } else if ("hsqldb".equals(dialect)) {
            pageSql.append(sql);
            pageSql.append(" LIMIT " + page.getPageSize() + " OFFSET "
                    + page.getStartRow());
        }
//        else if ("oracle".equals(dialect)) {
//            pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
//            pageSql.append(sql);
//            pageSql.append(" ) temp where rownum <= ").append(page.getEndRow());
//            pageSql.append(") where row_id > ").append(page.getStartRow());
//        }
        return pageSql.toString();
    }

    public static boolean sql_inj(String str) {
        String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
        String[] inj_stra = inj_str.split("\\|");
        for (int i = 0; i < inj_stra.length; i++) {
            if (str.indexOf(inj_stra[i]) >= 0) {
                return true;
            }
        }
        return false;

    }

    public String startPage(StatementHandler routingStatementHandler) {
        PageInfo pageInfo = ((PageInfo) (((Map) routingStatementHandler.getBoundSql().getParameterObject()).get("param")));
        return getPageSql(routingStatementHandler.getBoundSql().getSql(), pageInfo);
    }

    public static void main(String[] args) {
        System.out.println(sql_inj("update from "));
    }
}
