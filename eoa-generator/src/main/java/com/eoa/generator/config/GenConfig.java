package com.eoa.generator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author ChengLong
 * @ClassName: GenConfig
 * @Description 读取代码生成相关配置
 * @Date 2021/9/9 0009 13:55
 * @Version 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value = { "classpath:generator.yml" })
public class GenConfig {
    /** 作者 */
    public static String author;

    /** 生成包路径 */
    public static String packageName;

    /** 自动去除表前缀，默认是false */
    public static boolean autoRemovePre;

    /** 表前缀(类名不会包含表前缀) */
    public static String tablePrefix;

    public static String getAuthor() {
        return author;
    }

    @Value("${author}")
    public static void setAuthor(String author) {
        GenConfig.author = author;
    }

    public static String getPackageName() {
        return packageName;
    }

    @Value("${packageName}")
    public static void setPackageName(String packageName) {
        GenConfig.packageName = packageName;
    }

    public static boolean getAutoRemovePre() {
        return autoRemovePre;
    }

    @Value("${autoRemovePre}")
    public static void setAutoRemovePre(boolean autoRemovePre) {
        GenConfig.autoRemovePre = autoRemovePre;
    }

    public static String getTablePrefix() {
        return tablePrefix;
    }

    @Value("${tablePrefix}")
    public static void setTablePrefix(String tablePrefix) {
        GenConfig.tablePrefix = tablePrefix;
    }
}