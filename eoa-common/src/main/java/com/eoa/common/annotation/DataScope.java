package com.eoa.common.annotation;

import java.lang.annotation.*;

/**
 * @author ChengLong
 * @ClassName: DataSource
 * @Description 数据权限过滤注解
 * @Date 2021/9/9 0009 10:14
 * @Version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    /**
     * 部门表的别名
     */
    public String deptAlias() default "";

    /**
     * 用户表的别名
     */
    public String userAlias() default "";
}
