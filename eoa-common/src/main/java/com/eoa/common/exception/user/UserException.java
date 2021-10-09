package com.eoa.common.exception.user;

import com.eoa.common.exception.base.BaseException;

/**
 * @author ChengLong
 * @ClassName: UserException
 * @Description 用户信息异常类
 * @Date 2021/9/9 0009 11:03
 * @Version 1.0.0
 */
public class UserException extends BaseException {

    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
