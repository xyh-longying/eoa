package com.eoa.common.utils;

import com.eoa.common.core.text.Convert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author ChengLong
 * @ClassName: ServletUtils
 * @Description 客户端工具类
 * @Date 2021/9/8 0008 19:16
 * @Version 1.0.0
 */
public class ServletUtils {

    /**
     * 定义移动端请求的所有可能类型
     */
    private final static String[] agent = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser" };

    /**
     * 获取String参数
     * @param name
     * @return
     */
    public static String getParameter(String name){
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     * @param name
     * @param defaultValue
     * @return
     */
    public static String getParameter(String name, String defaultValue){
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     * @param name
     * @return
     */
    public static Integer getParameterToInt(String name){
        return Convert.toInt(getRequest().getParameter(name));
    }

    /**
     * 获取Integer参数
     * @param name
     * @param defaultValue
     * @return
     */
    public static Integer getParameterToInt(String name, Integer defaultValue){
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }
    
    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest(){
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取session
     * @return
     */
    public static HttpSession getSession(){
        return getRequest().getSession();
    }

    /**
     * 获取response
     * @return
     */
    public static HttpServletResponse getResponse(){
        return getRequestAttributes().getResponse();
    }
    
    public static ServletRequestAttributes getRequestAttributes(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) requestAttributes;
    }

    /**
     * 将字符串渲染到客户端
     * @param response
     * @param string
     * @return
     */
    public static String renderString(HttpServletResponse response, String string){
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否是Ajax异步请求
     * @param request
     * @return
     */
    public static Boolean isAjaxRequest(HttpServletRequest request){
        String accept = request.getHeader("accept");
        if(accept != null && accept.indexOf("application/json") != -1){
            return true;
        }
        
        String xRequestWith = request.getHeader("X-Requested-With");
        if(xRequestWith != null && xRequestWith.indexOf("XMLHttpRequest") != -1){
            return true;
        }
        
        String uri = request.getRequestURI();
        if(StringUtils.inStringIgnoreCase(uri, ".json", ".xml")){
            return true;
        }
        
        String ajax = request.getParameter("__ajax");
        if(StringUtils.inStringIgnoreCase(ajax, ".json", ".xml")){
            return true;
        }
        return false;
    }

    /**
     * 判断User-Agent 是不是来自于手机
     * @param ua
     * @return
     */
    public static Boolean checkAgentIsMobile(String ua){
        boolean flag = false;
        if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))){
            //排除苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")){
                for (String item : agent){
                    if(ua.contains(item)){
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }
}
