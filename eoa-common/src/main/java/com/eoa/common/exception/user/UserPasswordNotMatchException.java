package com.eoa.common.exception.user;

/**
 * @author ChengLong
 * @ClassName: UserPasswordNotMatchException
 * @Description 用户密码不正确或不符合规范异常类
 * @Date 2021/9/9 0009 11:07
 * @Version 1.0.0
 */
public class UserPasswordNotMatchException extends UserException{
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException()
    {
        super("user.password.not.match", null);
    }
}
