package com.eoa.common.annotation;

import com.eoa.common.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * @author ChengLong
 * @ClassName: DataSource
 * @Description 自定义多数据源切换注解
 * 优先级：先方法，后类，如果方法覆盖了类上的数据源类型，以方法的为准，否则以类上的为准
 * @Date 2021/9/9 0009 10:14
 * @Version 1.0.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {

    /**
     * 切换数据源名称
     */
    public DataSourceType value() default DataSourceType.MASTER;
}
