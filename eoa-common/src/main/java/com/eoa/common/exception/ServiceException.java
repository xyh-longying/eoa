package com.eoa.common.exception;

/**
 * @author ChengLong
 * @ClassName: ServiceException
 * @Description 业务异常
 * @Date 2021/9/9 0009 11:13
 * @Version 1.0.0
 */
public class ServiceException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     *
     * 和 {@link CommonResult#getDetailMessage()} 一致的设计
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException()
    {
    }

    public ServiceException(String message)
    {
        this.message = message;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    public ServiceException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }

    public String getMessage()
    {
        return message;
    }

    public ServiceException setMessage(String message)
    {
        this.message = message;
        return this;
    }
}
