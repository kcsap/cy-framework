package com.cy.framework.mybaties.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 表明注解可以被jvm编译
@Target(ElementType.METHOD) // 只能方法使用
public @interface Token {
    /**
     * 是否需要登录
     *
     * @return
     */
    boolean login() default false;

    /**
     * 是否需要后台登录
     *
     * @return
     */
    boolean admin() default false;

    /**
     * 是否可以用?
     *
     * @return
     */
    boolean available() default true;
}
