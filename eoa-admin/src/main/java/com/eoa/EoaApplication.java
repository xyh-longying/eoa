package com.eoa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author ChengLong
 * @ClassName: EoaApplication
 * @Description 启动程序
 * @Date 2021/9/7 0007 21:07
 * @Version 1.0.0
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class EoaApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(EoaApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  管理系统启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
