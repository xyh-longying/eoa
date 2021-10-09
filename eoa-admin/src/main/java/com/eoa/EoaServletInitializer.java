package com.eoa;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author ChengLong
 * @ClassName: EoaServletInitializer
 * @Description web容器部署
 * @Date 2021/9/7 0007 21:31
 * @Version 1.0.0
 */
public class EoaServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EoaApplication.class);
    }
}
