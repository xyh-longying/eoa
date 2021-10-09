package com.eoa.common.exception.user;

/**
 * @author ChengLong
 * @ClassName: UserPasswordRetryLimitExceedException
 * @Description 用户错误最大次数异常类
 * @Date 2021/9/9 0009 11:08
 * @Version 1.0.0
 */
public class UserPasswordRetryLimitExceedException extends UserException{
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitExceedException(int retryLimitCount)
    {
        super("user.password.retry.limit.exceed", new Object[] { retryLimitCount });
    }
}
