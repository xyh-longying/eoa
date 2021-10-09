package com.eoa.common.exception.user;

/**
 * @author ChengLong
 * @ClassName: UserDeleteException
 * @Description 用户账号已被删除
 * @Date 2021/9/9 0009 11:06
 * @Version 1.0.0
 */
public class UserDeleteException extends UserException{

    private static final long serialVersionUID = 1L;

    public UserDeleteException()
    {
        super("user.password.delete", null);
    }
}
