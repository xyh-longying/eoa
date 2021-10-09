package com.eoa.common.exception.user;

/**
 * @author ChengLong
 * @ClassName: CaptchaException
 * @Description 验证码错误异常类
 * @Date 2021/9/9 0009 11:03
 * @Version 1.0.0
 */
public class CaptchaException extends UserException{

    private static final long serialVersionUID = 1L;

    public CaptchaException()
    {
        super("user.jcaptcha.error", null);
    }
}
