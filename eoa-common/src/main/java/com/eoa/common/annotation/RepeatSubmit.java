package com.eoa.common.annotation;

import java.lang.annotation.*;

/**
 * @author ChengLong
 * @ClassName: RepeatSubmit
 * @Description 自定义注解防止表单重复提交
 * @Date 2021/9/9 0009 10:19
 * @Version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
}
