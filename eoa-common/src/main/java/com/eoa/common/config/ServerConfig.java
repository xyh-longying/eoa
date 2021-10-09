package com.eoa.common.config;

import com.eoa.common.utils.ServletUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ChengLong
 * @ClassName: ServerConfig
 * @Description 服务相关配置
 * @Date 2021/9/9 0009 9:50
 * @Version 1.0.0
 */
@Component
public class ServerConfig {

    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     * @return
     */
    public String getUrl(){
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }
    
    public String getDomain(HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
