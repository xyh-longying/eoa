package com.eoa.common.enums;

/**
 * @author ChengLong
 * @ClassName: UserStatus
 * @Description 用户状态
 * @Date 2021/9/9 0009 10:58
 * @Version 1.0.0
 */
public enum UserStatus {
    OK("0", "正常"), DISABLE("1", "停用"), DELETED("2", "删除");

    private final String code;
    private final String info;

    UserStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
