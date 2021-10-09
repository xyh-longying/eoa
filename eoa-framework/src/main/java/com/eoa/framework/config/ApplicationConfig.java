package com.eoa.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author ChengLong
 * @ClassName: ApplicationConfig
 * @Description 程序注解配置
 * @Date 2021/9/9 0009 17:55
 * @Version 1.0.0
 */
@Configuration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.eoa.**.mapper")
public class ApplicationConfig {
}
