package com.eoa.common.exception.user;

/**
 * @author ChengLong
 * @ClassName: UserNotExistsException
 * @Description 用户不存在异常类
 * @Date 2021/9/9 0009 11:07
 * @Version 1.0.0
 */
public class UserNotExistsException extends UserException{
    private static final long serialVersionUID = 1L;

    public UserNotExistsException()
    {
        super("user.not.exists", null);
    }
}
