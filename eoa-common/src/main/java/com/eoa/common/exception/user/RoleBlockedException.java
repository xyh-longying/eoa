package com.eoa.common.exception.user;

/**
 * @author ChengLong
 * @ClassName: RoleBlockedException
 * @Description 角色锁定异常类
 * @Date 2021/9/9 0009 11:05
 * @Version 1.0.0
 */
public class RoleBlockedException extends UserException{
    private static final long serialVersionUID = 1L;

    public RoleBlockedException()
    {
        super("role.blocked", null);
    }
}
