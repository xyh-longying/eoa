package com.eoa.common.exception;

/**
 * @author ChengLong
 * @ClassName: UtilException
 * @Description 工具类异常
 * @Date 2021/9/8 0008 11:49
 * @Version 1.0.0
 */
public class UtilException extends RuntimeException{

    private static final long serialVersionUID = 8247610319171014183L;
    
    public UtilException(Throwable e){
        super(e.getMessage(), e);
    }
    
    public UtilException(String message){
        super(message);
    }
    
    public UtilException(String message, Throwable throwable){
        super(message,throwable);
    }
}
