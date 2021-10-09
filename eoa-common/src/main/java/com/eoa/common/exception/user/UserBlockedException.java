package com.eoa.common.exception.user;

/**
 * @author ChengLong
 * @ClassName: UserBlockedException
 * @Description 用户锁定异常类
 * @Date 2021/9/9 0009 11:06
 * @Version 1.0.0
 */
public class UserBlockedException extends UserException{
    private static final long serialVersionUID = 1L;

    public UserBlockedException()
    {
        super("user.blocked", null);
    }
}
