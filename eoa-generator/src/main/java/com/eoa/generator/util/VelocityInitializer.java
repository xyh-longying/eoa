package com.eoa.generator.util;

import com.eoa.common.constant.Constants;
import org.apache.velocity.app.Velocity;

import java.util.Properties;

/**
 * @author ChengLong
 * @ClassName: VelocityInitializer
 * @Description VelocityEngine工厂
 * @Date 2021/9/9 0009 14:32
 * @Version 1.0.0
 */
public class VelocityInitializer {

    /**
     * 初始化vm方法
     */
    public static void initVelocity()
    {
        Properties p = new Properties();
        try
        {
            // 加载classpath目录下的vm文件
            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            // 定义字符集
            p.setProperty(Velocity.INPUT_ENCODING, Constants.UTF8);
            p.setProperty(Velocity.OUTPUT_ENCODING, Constants.UTF8);
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
