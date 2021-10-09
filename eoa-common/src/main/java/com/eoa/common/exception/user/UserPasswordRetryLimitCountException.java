package com.eoa.common.exception.user;

/**
 * @author ChengLong
 * @ClassName: UserPasswordRetryLimitCountException
 * @Description 用户错误记数异常类
 * @Date 2021/9/9 0009 11:08
 * @Version 1.0.0
 */
public class UserPasswordRetryLimitCountException extends UserException{
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitCountException(int retryLimitCount)
    {
        super("user.password.retry.limit.count", new Object[] { retryLimitCount });
    }
}
